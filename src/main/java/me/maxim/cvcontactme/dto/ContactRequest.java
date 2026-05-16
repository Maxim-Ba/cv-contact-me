package me.maxim.cvcontactme.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ContactRequest {

    @NotBlank(message = "Имя обязательно")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Сообщение обязательно")
    @Size(max = 5000, message = "Сообщение слишком длинное")
    private String message;
}
