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

    public int actualizarEmpleado(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ? WHERE id = ?";
        return jdbcTemplate.update(sql, empleado.getNombre(), empleado.getId());
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




}
