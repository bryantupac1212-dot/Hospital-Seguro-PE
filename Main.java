package hospitalsegurope;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
public class Main {
    static abstract class Recurso {
        protected int id;
        protected String codigo;
        protected String estado;
        public Recurso(int id, String codigo, String estado) {
            this.id = id;
            this.codigo = codigo;
            this.estado = estado;
        }
        public String getEstado() { return estado; }
        public String getCodigo() { return codigo; }
        public abstract String obtenerInfo();
    }
    static class Cama extends Recurso {
        private String area;
        public Cama(int id, String codigo, String estado, String area) {
            super(id, codigo, estado);
            this.area = area;
        }
        public String obtenerInfo() {
            return "Cama [" + codigo + "] - Estado: " + estado + " - Área: " + area;
        }
    }
    static class Medico extends Recurso {
        private String nombre;
        private String especialidad;
        public Medico(int id, String codigo, String estado, String nombre, String especialidad) {
            super(id, codigo, estado);
            this.nombre = nombre;
            this.especialidad = especialidad;
        }
        public String obtenerInfo() {
            return "Médico: Dr(a). " + nombre + " [" + codigo + "] - Esp: " + especialidad + " - Estado: " + estado;
        }
    }
    static class Medicamento extends Recurso {
        private String nombre;
        private int stock;
        public Medicamento(int id, String codigo, String estado, String nombre, int stock) {
            super(id, codigo, estado);
            this.nombre = nombre;
            this.stock = stock;
        }
        public int getStock() { return stock; }
        public String obtenerInfo() {
            return "Medicamento: " + nombre + " [" + codigo + "] - Cantidad: " + stock;
        }
    }
    static class ServicioHospital {
        private List<Recurso> listaRecursos = new ArrayList<>();
        public void agregar(Recurso r) {
            listaRecursos.add(r);
        }
        public List<Recurso> listarDisponibles() {
            return listaRecursos.stream()
                    .filter(r -> r.getEstado().equalsIgnoreCase("Disponible"))
                    .collect(Collectors.toList());
        }
        public List<Medicamento> listarInsumosCriticos(int min) {
            return listaRecursos.stream()
                    .filter(r -> r instanceof Medicamento)
                    .map(r -> (Medicamento) r)
                    .filter(m -> m.getStock() <= min)
                    .collect(Collectors.toList());
        }
        public List<Recurso> getLista() { return listaRecursos; }
    }
    public static void main(String[] args) {
        ServicioHospital servicio = new ServicioHospital();
        Scanner sc = new Scanner(System.in);
        servicio.agregar(new Cama(1, "CAM-01", "Disponible", "UCI"));
        servicio.agregar(new Cama(2, "CAM-02", "Ocupado", "Emergencia"));
        servicio.agregar(new Medico(3, "MED-01", "Disponible", "Carlos Ruiz", "Traumatología"));
        servicio.agregar(new Medicamento(4, "MEDIC-01", "Disponible", "Paracetamol", 100));
        servicio.agregar(new Medicamento(5, "MEDIC-02", "Disponible", "Suero Fisiológico", 10));
        int op = 0;
        do {
            System.out.println("\n--- GESTIÓN DE RECURSOS - ESSALUD ---");
            System.out.println("1. Ver todo el inventario");
            System.out.println("2. Consultar recursos DISPONIBLES");
            System.out.println("3. Reporte de Medicamentos Críticos (Stock <= 15)");
            System.out.println("4. Salir");
            System.out.print("Ingrese opción: ");
            if (sc.hasNextInt()) {
                op = sc.nextInt();
                switch (op) {
                    case 1:
                        System.out.println("\n--- INVENTARIO ---");
                        servicio.getLista().forEach(r -> System.out.println(r.obtenerInfo()));
                        break;
                    case 2:
                        System.out.println("\n--- DISPONIBLES ---");
                        servicio.listarDisponibles().forEach(r -> System.out.println(r.obtenerInfo()));
                        break;
                    case 3:
                        System.out.println("\n--- ALERTAS DE STOCK ---");
                        servicio.listarInsumosCriticos(15).forEach(m -> System.out.println(m.obtenerInfo()));
                        break;
                    case 4:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:1
                        System.out.println("Opción inválida.");
                }
            } else {
                System.out.println("Entrada no válida.");
                sc.next();
            }
        } while (op != 4);
    }
}