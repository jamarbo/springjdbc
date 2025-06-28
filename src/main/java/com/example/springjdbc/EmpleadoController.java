package com.example.springjdbc;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> listar() {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();

        // Convertimos de Entidad → DTO
        List<EmpleadoRespuestaDTO> respuesta = empleados.stream()
                .map(e -> new EmpleadoRespuestaDTO(e.getNombre()))
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/empleados")
    public ResponseEntity<String> insertar(@Valid @RequestBody Empleado empleado, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }

        empleadoDAO.insertarEmpleado(empleado);
        return ResponseEntity.ok("Empleado insertado exitosamente");
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
    public ResponseEntity<?> actualizar(@PathVariable int id, @Valid @RequestBody Empleado empleado, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }

        int filas = empleadoDAO.actualizarEmpleado(id, empleado.getNombre());

        if (filas > 0) {
            // Consulta el empleado actualizado y lo retorna
            Empleado actualizado = empleadoDAO.buscarPorId(id);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el empleado con ID " + id);
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

    @GetMapping(value = "/empleados", params = {"page", "size"})
    public ResponseEntity<?> listarPaginado(@RequestParam int page, @RequestParam int size) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body("Los parámetros 'page' y 'size' deben ser positivos.");
        }

        List<Empleado> empleados = empleadoDAO.obtenerPaginado(page, size);

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay resultados en esta página.");
        }

        return ResponseEntity.ok(empleados);
    }


    @GetMapping(value = "/empleados", params = {"nombre", "page", "size"})
    public ResponseEntity<?> buscarPorNombrePaginado(
            @RequestParam String nombre,
            @RequestParam int page,
            @RequestParam int size) {

        // 🧼 Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El parámetro 'nombre' es obligatorio.");
        }
        if (nombre.length() > 100) {
            return ResponseEntity.badRequest().body("El parámetro 'nombre' no debe superar los 100 caracteres.");
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            return ResponseEntity.badRequest().body("El parámetro 'nombre' contiene caracteres no permitidos.");
        }
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body("Los parámetros 'page' y 'size' deben ser positivos.");
        }

        // 🔢 Total de resultados para este filtro
        long totalResultados = empleadoDAO.contarPorNombre(nombre);

        // 📦 Lista paginada
        List<Empleado> resultados = empleadoDAO.buscarPorNombrePaginado(nombre, page, size);

        if (resultados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron empleados con ese nombre en esta página.");
        }

        // 📤 Envolver en respuesta con metadatos
        PaginacionRespuesta<Empleado> respuesta = new PaginacionRespuesta<>(
                page,
                size,
                totalResultados,
                resultados
        );

        return ResponseEntity.ok(respuesta);
    }






}
