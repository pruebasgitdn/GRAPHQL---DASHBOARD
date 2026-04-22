package com.back.services;
import com.back.entities.Notification;
import com.back.entities.User;
import com.back.entities.dto.NotificationResponse;
import com.back.entities.dto.WorkspaceMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationPublisherService {


    //ConcurrentHashMap => asegura que el acceso y las modificaciones del mapa
    // sean seguros para múltiples hilos permitiendo escritura y lectura en tiemp real


    // Mapea cada userId a un sinks.many


    //Almacentamiento de sinks (sockets)
    //Sink.Many => emisor seguro para hilos, capaz de empujar muiltples eventos
    // hacia 1 o varios (N) suscriptores
    private final Map<String, Sinks.Many<NotificationResponse>> userSinks = new ConcurrentHashMap<>();




    //Flux => clase utilizada para  manejar flujos d data asincronos
   // Secuencia de 0 a N elementos => Notificaiones en este caso
    //Flijo de datos reactivos
    public Flux<NotificationResponse> subscribe(String userId) {

        //computeifAbsent => intenta coger el sinkmany<Notification> para ese userID
        //Si no existe lo crea con las sgte config:

        //sinks.many() => crea u tipo de sink que puede emitir valores a multiples subscrip
        //multicast() => permite emitir valores a varios susbcriptores al instante
        Sinks.Many<NotificationResponse> sink =
                userSinks.computeIfAbsent(
                        userId,
                        id -> Sinks.many().multicast().onBackpressureBuffer()
                );

        return sink.asFlux()
                .doOnSubscribe(sub -> {
                    System.out.println("Usuario suscrito: " + userId);
                })
                .doFinally(signal -> {
                    if (sink.currentSubscriberCount() == 0) {
                        userSinks.remove(userId);
                    }
                });


        //onBackPressure => manejo de retroceso (backpressure)
        //Si los users no pueden manejar el ritmo de emisiones de notificaiones
        //se almacenan en bufer hasta q las pueda procesar


        // asFlux() convierte el Sinks.Many en un Flux<Notification>,
        // el cual es un tipo de flujo reactivo que permite a los suscriptores
        // recibir las notificaciones.
    }



    //Metodo de emision a ese userId , la notificaion
    public void publish(String userId, NotificationResponse notification) {

        Sinks.Many<NotificationResponse> sink = userSinks.get(userId);
        // obtener el sink que corresponde al userId del userSinks anterior
        // donde se subscribe

        //Si no esta vacio emitir ,intenar emitir
        if (sink != null) {
            sink.tryEmitNext(notification);
        }
    }

//    public void publishAddedToWorkspace(String userId, NotificationResponse notification
//            , WorkspaceMemberResponse workspaceMemberResponse) {
//
//        Sinks.Many<NotificationResponse> sink = userSinks.get(userId);
//        // obtener el sink que corresponde al userId del userSinks anterior
//        // donde se subscribe
//
//        //Si no esta vacio emitir ,intenar emitir
//        if (sink != null) {
//            sink.tryEmitNext(workspaceMemberResponse);
//        }
//    }


    public List<String> getActiveUsers() {
        // Filtra los usuarios que tienen un Sinks.Many activo
        return userSinks.entrySet().stream()
                .filter(entry -> entry.getValue().currentSubscriberCount() > 0) // Verifica si hay suscriptores activos
                .map(Map.Entry::getKey) // Extrae el userId de los usuarios activos
                .collect(Collectors.toList());
    }


}
