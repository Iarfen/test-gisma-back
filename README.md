# Back-end test de Gisma
## Instalar base de datos
Primero se debe instalar la base de datos de MariaDB con el archivo bd.sql. El usuario, si no es root, debe ser configurado en el archivo **src/main/resources/application.properties**.

## Iniciar la aplicación
Tras instalar la base de datos y configurar el usuario, si no es el usuario root, se debe iniciar la aplicación. Para ello se utiliza el comando siguiente desde el directorio de la aplicación:
```
./gradlew bootRun
```

## Ejecutar los test
Para ejecutar los test se utiliza IntelliJ, se abre el archivo de **TestGismaTests.java** y se ejecutan los test desde el IDE.