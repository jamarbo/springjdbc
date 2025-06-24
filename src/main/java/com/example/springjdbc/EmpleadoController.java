package com.example.springjdbc;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class EmpleadoController {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoController(EmpleadoDAO empleadoDAO) {
          this.empleadoDAO =  empleadoDAO;

    }

    @GetMapping("/empleados")
    public List<Empleado> listar() {
        return empleadoDAO.obtenerTodos();
    }


    @PostMapping("/empleados")
    public ResponseEntity<String> insertar(@Valid @RequestBody Empleado empleado, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        try {
            empleadoDAO.insertarEmpleado(empleado);
            return ResponseEntity.ok("Empleado insertado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al insertar: " + e.getMessage());
        }
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            Empleado empleado = empleadoDAO.buscarPorId(id);
            return ResponseEntity.ok(empleado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Empleado no encontrado con ID: " + id);
        }
    }

    @PutMapping("/empleados/{id}")

    public ResponseEntity<String> actualizar(@PathVariable int id, @Valid @RequestBody Empleado empleado, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        try {
            empleado.setId(id);
            int filasActualizadas = empleadoDAO.actualizarEmpleado(empleado);
            if (filasActualizadas > 0) {
                return ResponseEntity.ok("Empleado actualizado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Empleado no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        try {
            int filasEliminadas = empleadoDAO.eliminarEmpleado(id);
            if (filasEliminadas > 0) {
                return ResponseEntity.ok("Empleado eliminado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Empleado no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar: " + e.getMessage());
        }
    }

    @GetMapping(value = "/empleados", params = "nombre")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        // Validación 1: nombre no puede ser nulo o vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El parámetro 'nombre' es obligatorio.");
        }

        // Validación 2: longitud máxima
        if (nombre.length() > 100) {
            return ResponseEntity.badRequest().body("El parámetro 'nombre' no debe superar los 100 caracteres.");
        }

        // Validación 3: solo letras, espacios y tildes
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            return ResponseEntity.badRequest().body("El parámetro 'nombre' contiene caracteres no permitidos.");
        }
        // Búsqueda segura
        List<Empleado> empleados = empleadoDAO.buscarPorNombre(nombre);

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron empleados con nombre: " + nombre);
        }
        return ResponseEntity.ok(empleados);
    }


}
