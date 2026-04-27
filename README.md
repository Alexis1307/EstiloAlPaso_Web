Comfiguracion Inicial:
Para que tu proyecto funcione en local debes configurar la siguiente variable de entorno:
JWT_SECRET: Tu clave secreta para la firma de tokens ejemplo: ("mi_clave_super_mega_ultra_secreta")

Autenticacion: 
El sistema utiliza Tokens, sigue estos pasos para que puedas probar los endpoints:
Obtener token mediante el endpoint de Login, y configurar el Header: 

"Authorization: Bearer {token}",
eso en cada endpoint para poder probar su funcionamiento.

Credenciales de Prueba:

Admin: (estas credenciales a la hora de ejecutar el proyecto se crean de manera automatica)

{
"usuario": "AlxisJara",
"password": "Alexis1307"
}

De igual forma, adjuntare un video explicando paso a paso como puedes configurar, crear y probar el token
