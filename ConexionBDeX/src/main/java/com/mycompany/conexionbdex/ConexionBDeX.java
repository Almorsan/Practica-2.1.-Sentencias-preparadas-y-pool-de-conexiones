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

public class ConexionBDeX {

    //static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
    //static final String USER = "alejandro";
    //static final String PASS = "1234";
    private static final PoolDataSource pool;

    static {
        try {
            pool = PoolDataSourceFactory.getPoolDataSource();
            pool.setConnectionFactoryClassName("com.mysql.cj.jdbc.MysqlDataSource");
            pool.setURL("jdbc:mysql://localhost:3306/jcvd");
            pool.setUser("alejandro");
            pool.setPassword("1234");
            pool.setInitialPoolSize(10);
            pool.setMinPoolSize(10);
            pool.setMaxPoolSize(50);
        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido configurar correctamente el pool de conexiones", e);
        }
    }

    public static void main(String[] args) {

        boolean prueba;

        String nombre = "Castlevania";

        prueba = buscaNombre(nombre);

        System.out.println(prueba);
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        String nombreVideojuego = "Megaman X";

        String categoria = "Accion";

        Date fecha = new Date(93, 12, 17);

        String compania = "Capcom";

        float precio = 39.99f;

        Videojuego nuevoJuego = new Videojuego(nombreVideojuego, categoria, fecha, compania, precio);

        nuevoRegistro(nuevoJuego);

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        lanzaConsulta();

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        String nombre2 = "Megaman X";

        //si no está comentado este método, borrará el videojuego creado anteriormente
        boolean eliminado = EliminarRegistro(nombre2);

        System.out.println(eliminado);

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        nuevoRegistro2();

    }

    public static boolean buscaNombre(String nombre) {
        boolean encontrado = false;
        Connection conn = null;

        String QUERY = "SELECT * FROM videojuegos WHERE NOMBRE=?";

        System.out.println("Buscando información sobre el videojuego llamado " + nombre);

        try {
            conn = pool.getConnection();

            try (PreparedStatement sentencia = conn.prepareStatement(QUERY)) {
                sentencia.setString(1, nombre);

                try (ResultSet rs = sentencia.executeQuery()) {
                    while (rs.next()) {
                        String nombreVideojuego = rs.getString("Nombre");

                        if (nombreVideojuego != null) {
                            System.out.println("ID: " + rs.getInt("id"));
                            System.out.println("Nombre: " + nombreVideojuego);
                            System.out.println("Categoría: " + rs.getString("Categoría"));
                            System.out.println("Fecha Lanzamiento: " + rs.getDate("Fecha_lanzamiento"));
                            System.out.println("Compañía: " + rs.getString("Compañía"));
                            System.out.println("Precio: " + rs.getFloat("Precio"));
                            System.out.println("");

                            encontrado = true;
                        }
                    }

                    if (!encontrado) {
                        System.out.println("No se ha encontrado videojuego con el nombre " + nombre);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return encontrado;
    }

    public static void lanzaConsulta() {
        String nombre = "Metroid Prime";
        String QUERY = "SELECT * FROM videojuegos WHERE nombre= ?";

        Connection conn = null;

        try {
            conn = pool.getConnection();
            try (PreparedStatement sentencia = conn.prepareStatement(QUERY)) {

                sentencia.setString(1, nombre);

                try (ResultSet rs = sentencia.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("id"));
                        System.out.println("Nombre: " + rs.getString("Nombre"));
                        System.out.println("Categoría: " + rs.getString("Categoría"));
                        System.out.println("Fecha Lanzamiento: " + rs.getDate("Fecha_lanzamiento"));
                        System.out.println("Compañía: " + rs.getString("Compañía"));
                        System.out.println("Precio: " + rs.getFloat("Precio"));
                        System.out.println("");
                        System.out.println("");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión en el bloque finally
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void nuevoRegistro(Videojuego juego) {
        String QUERY = "INSERT INTO videojuegos (Nombre, Categoría, Fecha_lanzamiento, Compañía, Precio) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = conn = pool.getConnection();
            try (PreparedStatement sentencia = conn.prepareStatement(QUERY)) {

                sentencia.setString(1, juego.getNombre());
                sentencia.setString(2, juego.getCategoria());
                sentencia.setDate(3, juego.getFecha_Lanzamiento());
                sentencia.setString(4, juego.getCompania());
                sentencia.setFloat(5, juego.getPrecio());

                int rowsAffected = sentencia.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Nuevo registro realizado con éxito");
                } else {
                    System.out.println("No se ha podido realizar el nuevo registro");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean EliminarRegistro(String nombre) {

        boolean eliminado = false;

        String QUERY = "DELETE FROM videojuegos WHERE Nombre=?";

        System.out.println("Borrando el videojuego con el nombre " + nombre);

        Connection conn = null;
        PreparedStatement sentencia = null;

        try {
            conn = pool.getConnection();
            sentencia = conn.prepareStatement(QUERY);

            sentencia.setString(1, nombre);
            int rowsAffected = sentencia.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Se ha borrado con éxito");
                eliminado = true;
            } else {
                System.out.println("No se ha borrado ningún videojuego");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión en el bloque finally
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return eliminado;

    }

    public static void nuevoRegistro2() {
        String nombre = "";
        String categoria = "";
        Date fechaLanzamiento = null;
        String compania = "";
        float precio;
        Scanner teclado = new Scanner(System.in);

        int longitud = 0;

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

        System.out.println("Dime el año de lanzamiento, sólo las dos últimas cifras (por ejemplo, 87 para 1987): ");
        int ano = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Dime el número de mes de lanzamiento: ");
        int mes = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Dime el día de lanzamiento: ");
        int dia = teclado.nextInt();
        teclado.nextLine();

        // Crear el objeto Date
        fechaLanzamiento = new Date(ano, mes, dia);

        System.out.println("Dime la compañía: ");
        compania = teclado.nextLine().trim();
        System.out.println("Compania ingresada: " + compania);

        System.out.println("Dime el precio: ");
        precio = teclado.nextFloat();

        Connection conn = null;

        try {
            conn = pool.getConnection();

            Videojuego juego = new Videojuego(nombre, categoria, fechaLanzamiento, compania, precio);

            nuevoRegistro(juego);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
