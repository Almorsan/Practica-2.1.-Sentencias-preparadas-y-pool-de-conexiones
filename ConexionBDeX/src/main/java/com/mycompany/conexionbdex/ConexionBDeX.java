package com.mycompany.conexionbdex;

import static com.mysql.cj.util.SaslPrep.StringType.QUERY;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import java.time.LocalDate;

public class ConexionBDeX {
    
    //declaramos el objeto PoolDataSource como una constante static
    //La clase PoolDataSource nos permite configurar el número de conexiones
    //iniciales, así como el número de conexiones máximas y mínimas
    //permitidas
    

    private static final PoolDataSource POOL = PoolDataSourceFactory.getPoolDataSource();

    
    


    public static void main(String[] args) {

        try {

            POOL.setConnectionFactoryClassName("com.mysql.cj.jdbc.MysqlDataSource"); //establecemos el tipo
            //de conexión
            POOL.setURL("jdbc:mysql://localhost:3306/jcvd"); //establecemos la dirección de nuestra
            //base de datos
            POOL.setUser("alejandro"); //establecemos nuestro nombre de usuario
            POOL.setPassword("1234"); //establecemos nuestra contraseña
            POOL.setInitialPoolSize(10); //establecemos el tamaño inicial del pool
            POOL.setMinPoolSize(10); //establecemos el tamaño mínimo del pool
            POOL.setMaxPoolSize(50); //establecemos el tamaño máximo del pool
        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido configurar correctamente el pool de conexiones", e);
        }

        //comprobamos si un videojuego se encuentra en nuestra base de 
        //datos pasandole el nombre del mismo
        boolean prueba;

        String nombre = "Castlevania";

        prueba = buscaNombre(nombre);

        System.out.println(prueba);
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        //añadimos un videojuego a nuestra base de datos
        //mediante la construcción de un objeto Videojuego
        String nombreVideojuego = "Megaman X";

        String categoria = "Accion";

        LocalDate fecha = LocalDate.of(1993, 12, 17);

        String compania = "Capcom";

        float precio = 39.99f;

        Videojuego nuevoJuego = new Videojuego(nombreVideojuego, categoria, fecha, compania, precio);

        nuevoRegistro(nuevoJuego);

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        //lanzamos una consulta
        lanzaConsulta();

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        //borramos un videojuego de la base de datos
        //mediante un nombre pasado como parámetro
        String nombre2 = "Megaman X";

        //si no está comentado este método, borrará el videojuego creado anteriormente
        boolean eliminado = EliminarRegistro(nombre2);

        System.out.println(eliminado);

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        //efectuamos un nuevo registro mediante 
        //la entrada por teclado (Scanner)
        nuevoRegistro2();

    }

    public static boolean buscaNombre(String nombre) {
        //con este método, buscamos un videojuego en la base de datos
        //mediante un nombre pasado como parámetro. Si el juego existe
        //devolvemos un boolean de valor true. Si no, el valor del
        //boolean será false
        //Utilizaremos las clases Connection, PreparedStatement
        //y ResultSet
        boolean encontrado = false;
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        String QUERY = "SELECT * FROM videojuegos WHERE NOMBRE=?";
        //utilizamos la consulta de arriba para buscar el juego
        System.out.println("Buscando información sobre el videojuego llamado " + nombre);

        try {
            conexion = POOL.getConnection();
            sentencia = conexion.prepareStatement(QUERY);
            sentencia.setString(1, nombre);
            resultado = sentencia.executeQuery();
            while (resultado.next()) { //mientra se lea en la base de datos
                String nombreVideojuego = resultado.getString("Nombre"); //se guarda el nombre del 
                //videojuego que se esté leyendo en ese momento en una variable

                if (nombreVideojuego != null) { //si el nombre no es null
                    //mostramos la información del juego
                    System.out.println("ID: " + resultado.getInt("id"));
                    System.out.println("Nombre: " + nombreVideojuego);
                    System.out.println("Categoría: " + resultado.getString("Categoría"));
                    System.out.println("Fecha Lanzamiento: " + resultado.getDate("Fecha_lanzamiento"));
                    System.out.println("Compañía: " + resultado.getString("Compañía"));
                    System.out.println("Precio: " + resultado.getFloat("Precio"));
                    System.out.println("");

                    encontrado = true; //cambiamos el valor de encontrado a true
                }
            }

            if (!encontrado) { //si encontrado es false
                System.out.println("No se ha encontrado videojuego con el nombre " + nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) { //si la conexión no es null
                    //y no se ha cerrado
                    conexion.close(); //cerramos la conexión
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return encontrado; //devolvemos el valor de encontrado
    }

    public static void lanzaConsulta() {
        //lanzamos una consulta para ver la información de un juego en contreto
        String nombre = "Metroid Prime";
        String QUERY = "SELECT * FROM videojuegos WHERE nombre= ?";

        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        try {
            conexion = POOL.getConnection();
            sentencia = conexion.prepareStatement(QUERY);
            sentencia.setString(1, nombre);
            resultado = sentencia.executeQuery();
            while (resultado.next()) { //mientras se lea la base de datos 
                //mostramos la información del juego
                System.out.println("ID: " + resultado.getInt("id"));
                System.out.println("Nombre: " + resultado.getString("Nombre"));
                System.out.println("Categoría: " + resultado.getString("Categoría"));
                System.out.println("Fecha Lanzamiento: " + resultado.getDate("Fecha_lanzamiento"));
                System.out.println("Compañía: " + resultado.getString("Compañía"));
                System.out.println("Precio: " + resultado.getFloat("Precio"));
                System.out.println("");
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (conexion != null && !conexion.isClosed()) { //si la conexión no es null
                    //y no está cerrada
                    conexion.close(); //cerramos  la conexión
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void nuevoRegistro(Videojuego juego) {
        //añadimos un nuevo registro a la base de datos 
        //mediante un objeto de tipo Videojuego pasado por teclado

        String QUERY = "INSERT INTO videojuegos (Nombre, Categoría, Fecha_lanzamiento, Compañía, Precio) VALUES (?, ?, ?, ?, ?)";

        Connection conexion = null;
        PreparedStatement sentencia = null;

        try {
            conexion = POOL.getConnection();
            sentencia = conexion.prepareStatement(QUERY);
            //utlizamos los getters y setters de la clase Videojuego
            sentencia.setString(1, juego.getNombre());
            sentencia.setString(2, juego.getCategoria());
            //parseamos la fecha LocalDate de la clase Videojuego 
            //a una admitida por la base de datos
            Date fechaSql = Date.valueOf(juego.getFecha());
            sentencia.setDate(3, fechaSql);
            sentencia.setString(4, juego.getCompania());
            sentencia.setFloat(5, juego.getPrecio());

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Nuevo registro realizado con éxito");
            } else {
                System.out.println("No se ha podido realizar el nuevo registro");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (conexion != null && !conexion.isClosed()) { //si la conexión no es nula
                    //y no se ha cerrado
                    conexion.close(); //cerramos la conexión
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean EliminarRegistro(String nombre) {
        //con este método, eliminamos un videojuego
        //de la base de datos mediante un nombre
        //pasado como parámetro
        //devolvemos un boolean que será true si
        //se elimina y false si no

        boolean eliminado = false;

        String QUERY = "DELETE FROM videojuegos WHERE Nombre=?";

        System.out.println("Borrando el videojuego con el nombre " + nombre);
        //la sentencia de arriba borrará el videjuego con el nombre pasado
        //como parámetro

        Connection conexion = null;
        PreparedStatement sentencia = null;

        try {
            conexion = POOL.getConnection();
            sentencia = conexion.prepareStatement(QUERY);

            sentencia.setString(1, nombre);
            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Se ha borrado con éxito");
                eliminado = true; //modificado el valor de eliminado 
                //en caso de que se haya borrado la fila
            } else {
                System.out.println("No se ha borrado ningún videojuego");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexion != null && !conexion.isClosed()) { //si la conexión no es nula
                    //y no se ha cerrado
                    conexion.close(); //cerramos la conexión
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return eliminado;

    }

    public static void nuevoRegistro2() {
        //añadimos a la base de datos un nuevo juego 
        //mediante entrada por teclado
        String nombre = "";
        String categoria = "";
        LocalDate fechaLanzamiento = null;
        String compania = "";
        float precio;
        Scanner teclado = new Scanner(System.in);

        int longitud = 0;

        //ya que el nombre es un campo obligatorio, comprobaremos
        //que el usuario lo ha introducido 
        while (longitud == 0) {
            System.out.println("Dime el nombre del videojuego: ");
            nombre = teclado.nextLine().trim();
            longitud = nombre.length();

            if (longitud == 0) {
                System.out.println("Error, introduzca un nombre");
                System.out.println("");
            }
        }

        System.out.println("Dime la categoría: ");
        categoria = teclado.nextLine().trim();

        System.out.println("Dime el año de lanzamiento: ");
        int ano = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Dime el número de mes de lanzamiento: ");
        int mes = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Dime el día de lanzamiento: ");
        int dia = teclado.nextInt();
        teclado.nextLine();

        // Crear el objeto Date
        fechaLanzamiento = LocalDate.of(ano, mes, dia);

        System.out.println("Dime la compañía: ");
        compania = teclado.nextLine().trim();
        System.out.println("Compania ingresada: " + compania);

        System.out.println("Dime el precio: ");
        precio = teclado.nextFloat();

        Videojuego juego = new Videojuego(nombre, categoria, fechaLanzamiento, compania, precio);

        //llamamos al método nuevoRegistro
        nuevoRegistro(juego);

    }
}
