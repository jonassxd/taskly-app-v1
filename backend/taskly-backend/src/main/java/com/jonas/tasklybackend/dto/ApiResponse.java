package com.jonas.tasklybackend.dto;

public class ApiResponse<T> {

    private int status;
    private T data;
    private String mensagem;

    public ApiResponse(int status, T data, String mensagem) {
        this.status = status;
        this.data = data;
        this.mensagem = mensagem;
    }

    // SUCESSO
    public static <T> ApiResponse<T> sucesso(int status, T data) {
        return new ApiResponse<>(status, data, null);
    }

    // ERRO
    public static <T> ApiResponse<T> erro(int status, String mensagem) {
        return new ApiResponse<>(status, null, mensagem);
    }

    public int getStatus() { return status; }
    public T getData() { return data; }
    public String getMensagem() { return mensagem; }
}