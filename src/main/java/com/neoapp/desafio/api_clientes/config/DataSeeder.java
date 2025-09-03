package com.neoapp.desafio.api_clientes.config;

import com.neoapp.desafio.api_clientes.model.Role;
import com.neoapp.desafio.api_clientes.model.Usuario;
import com.neoapp.desafio.api_clientes.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev") // A classe só será executada quando o perfil "dev" estiver ativo em application.properties
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        criarAdminSeNaoExistir();
    }

    private void criarAdminSeNaoExistir() {
        String adminEmail = "admin@neoapp.com";

        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            System.out.println("Criando usuário ADMIN padrão...");

            Usuario admin = new Usuario();
            admin.setEmail(adminEmail);

            admin.setSenha(passwordEncoder.encode("admin123"));

            admin.setRole(Role.ADMIN);
            admin.setCliente(null);

            usuarioRepository.save(admin);

            System.out.println("Usuário ADMIN criado com sucesso!");
        } else {
            System.out.println("Usuário ADMIN já existe. Nenhuma ação necessária.");
        }
    }
}