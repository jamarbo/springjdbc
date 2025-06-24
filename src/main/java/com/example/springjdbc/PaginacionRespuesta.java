package com.example.springjdbc;

import java.util.List;

public class PaginacionRespuesta<T> {

    private int pagina;
    private int tamaño;
    private long totalResultados;
    private int totalPaginas;
    private List<T> resultados;

    public PaginacionRespuesta() {}

    public PaginacionRespuesta(int pagina, int tamaño, long totalResultados, List<T> resultados) {
        this.pagina = pagina;
        this.tamaño = tamaño;
        this.totalResultados = totalResultados;
        this.resultados = resultados;
        this.totalPaginas = (int) Math.ceil((double) totalResultados / tamaño);
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public List<T> getResultados() {
        return resultados;
    }

    public void setResultados(List<T> resultados) {
        this.resultados = resultados;
    }
}
