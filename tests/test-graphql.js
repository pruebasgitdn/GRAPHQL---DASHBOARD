import http from 'k6/http';
import { check, sleep } from 'k6';

// Opciones de prueba: usuarios virtuales y duración
export let options = {
  vus: 5,         // 5 usuarios virtuales
  duration: '30s' // duración total 30 segundos
};

// Token JWT de ejemplo (si tu app requiere auth)
//const JWT_TOKEN = 'TU_TOKEN_AQUI';

export default function () {
  const url = 'http://localhost:8080/graphql';

  const payload = JSON.stringify({
    query: `
     query UserList {
         userList {
             id
             name
             email
             profilePic
         }
     }

    `,
    //variables: { id: "1" } // ejemplo de variable
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      //'Authorization': `Bearer ${JWT_TOKEN}`
    },
  };

  // Ejecutar request
  const res = http.post(url, payload, params);

  // Validaciones
  check(res, {
    'status es 200': (r) => r.status === 200,
    'respuesta contiene user': (r) => r.body.includes('user')
  });

  // Simula tiempo entre requests
  sleep(1);
}


//Peticiones simples (GET, POST, GraphQL).
//check() y validaciones.
//Manejo de autenticación (obtener y reutilizar un JWT).
//Encadenar varias peticiones para simular un flujo de usuario.
//Configurar distintos perfiles de carga con vus, duration y stages.
//Usar scenarios para representar distintos tipos de usuarios concurrentes.
//Añadir thresholds para definir criterios de éxito (por ejemplo, "el 95 % de las respuestas debe tardar menos de 500 ms" y "la tasa de errores debe ser menor al 1 %").