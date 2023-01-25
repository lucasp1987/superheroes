package com.example.superheroes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "superhero")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Superhero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String prefix;
    private String suffix;
    private String power;
    private String descriptor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Superhero superhero = (Superhero) o;
        return Objects.equals(id, superhero.id) && Objects.equals(name, superhero.name) && Objects.equals(prefix, superhero.prefix) && Objects.equals(suffix, superhero.suffix) && Objects.equals(power, superhero.power) && Objects.equals(descriptor, superhero.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, prefix, suffix, power, descriptor);
    }

    @Override
    public String toString() {
        return "Superhero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", power='" + power + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}
