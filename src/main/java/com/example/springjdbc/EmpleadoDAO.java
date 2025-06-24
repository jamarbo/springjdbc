package com.example.springjdbc;
import  org.springframework.jdbc.core.JdbcTemplate;
import  org.springframework.stereotype.Repository;
import  java.util.List;

@Repository
public class EmpleadoDAO {

    private final JdbcTemplate jdbcTemplate;

    public EmpleadoDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertarEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre) VALUES (?)";
        jdbcTemplate.update(sql, empleado.getNombre());
    }

    public List<Empleado> obtenerTodos() {
        return jdbcTemplate.query("SELECT id, nombre FROM empleados",
                (rs, rowNum) -> {
                    Empleado e = new Empleado();
                    e.setId(rs.getInt("id"));
                    e.setNombre(rs.getString("nombre"));
                    return e;
                });
    }



    public Empleado buscarPorId(int id){

        String sql = "SELECT id, nombre FROM empleados WHERE id = ? ";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                ((rs, rowNum) -> {
                    Empleado e  = new Empleado();
                    e.setId(rs.getInt("id"));
                    e.setNombre(rs.getString("nombre"));
                    return e;
                }
        ));

    }

    public int actualizarEmpleado(int id, String nombre) {
        String sql = "UPDATE empleados SET nombre = ? WHERE id = ?";
        return jdbcTemplate.update(sql, nombre, id);
    }


    public int eliminarEmpleado(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Empleado> buscarPorNombre(String nombre){
         String sql = "SELECT id, nombre FROM empleados WHERE LOWER(nombre) LIKE ?";
         String criterio = "%"+nombre.toLowerCase()+"%";

         return jdbcTemplate.query(sql,
                             new Object[]{criterio},
                             (rs, rowNum)-> {
                     Empleado e = new Empleado();
                     e.setId(rs.getInt("id"));
                     e.setNombre(rs.getString("nombre"));
                     return e;
                 });
    }

    public List<Empleado> obtenerPaginado(int page, int size) {
        String sql = "SELECT id, nombre FROM empleados ORDER BY id LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
            Empleado e = new Empleado();
            e.setId(rs.getInt("id"));
            e.setNombre(rs.getString("nombre"));
            return e;
        });
    }

    public long contarPorNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM empleados WHERE LOWER(nombre) LIKE ?";
        String criterio = "%" + nombre.toLowerCase() + "%";
        return jdbcTemplate.queryForObject(sql, new Object[]{criterio}, Long.class);
    }


    public List<Empleado> buscarPorNombrePaginado(String nombre, int page, int size) {
        String sql = "SELECT id, nombre FROM empleados " +
                "WHERE LOWER(nombre) LIKE ? " +
                "ORDER BY id " +
                "LIMIT ? OFFSET ?";
        String criterio = "%" + nombre.toLowerCase() + "%";
        int offset = page * size;

        return jdbcTemplate.query(sql,
                new Object[]{criterio, size, offset},
                (rs, rowNum) -> {
                    Empleado e = new Empleado();
                    e.setId(rs.getInt("id"));
                    e.setNombre(rs.getString("nombre"));
                    return e;
                });
    }




}
