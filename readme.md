Para clonar el repositorio y pararte en la raiz
git clone https://github.com/tu-usuario/Challenge.git
cd Challenge


Construir y levantar todo:

docker pull tebymon/challenge-api
ejecucion:
docker-compose up --build







Decisiones técnicas del proyecto

##############################
Decisión Técnica: Cambio de versión de Java
Motivación:
Para garantizar la estabilidad del ambiente de desarrollo del challenge, se tomó la decisión de utilizar Java 17 en lugar de Java 21.

Justificación:
La biblioteca Lombok, utilizada ampliamente para simplificar el código mediante anotaciones como @Getter, @Setter, @AllArgsConstructor, entre otras, no tiene actualmente un soporte totalmente estable para Java 21. Aunque existe una versión experimental compatible, esta presenta riesgos de compatibilidad que podrían afectar la robustez y mantenibilidad del sistema.

Conclusión:
Java 17 es una versión LTS (Long-Term Support) ampliamente utilizada y probada, lo que ofrece un entorno más seguro y confiable para el desarrollo del proyecto.

##############################



 Estructura del proyecto (Hexagonal)
Organizamos el proyecto siguiendo la arquitectura hexagonal. Esto nos ayuda a separar:

La lógica del negocio (cálculo y reglas)

La forma en que entran los datos (controladores REST)

Y cómo se guardan los datos (base de datos PostgreSQL)

Esto hace que el código sea más ordenado, fácil de probar y mantener.

Servicio de cálculo con porcentaje
El cálculo funciona así:

Suma dos números.

Consulta un porcentaje desde un servicio externo.

Aplica ese porcentaje al resultado.

Devuelve el resultado final.

Para mejorar el rendimiento:

Usamos caché: si ya tenemos el porcentaje, lo guardamos 30 minutos.

Si el servicio externo falla, intentamos 3 veces con @Retry.

Si no hay valor en caché, mostramos un error claro.

 Documentación con Swagger
La API está documentada con Swagger UI usando springdoc-openapi.

Creamos interfaces (CalculatorApi, CalculationHistoryApi) que contienen las anotaciones de Swagger.

Los controladores implementan estas interfaces para mantener el código más limpio.

Así puedes ver y probar la API fácilmente en http://localhost:8080/swagger-ui.html.

 Pruebas
Agregamos tests unitarios para verificar:

Que los cálculos sean correctos.

Que los errores se manejen bien.

Que la consulta del historial paginado funcione correctamente.

También simulamos fallos del servicio externo para probar los reintentos y el uso del caché.

 Herramientas usadas
Spring Boot – Para crear la API.

Spring Data JPA – Para trabajar con la base de datos.

PostgreSQL – Nuestra base de datos, corre en Docker.

Spring Cache + Resilience4j – Para mejorar rendimiento y manejar fallos.

Swagger – Para documentar la API.

Lombok – Para evitar escribir mucho código repetitivo.

