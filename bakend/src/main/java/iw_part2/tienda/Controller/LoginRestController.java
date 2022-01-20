package iw_part2.tienda.Controller;

import iw_part2.tienda.Model.Group;
import iw_part2.tienda.Model.GroupRepository;
import iw_part2.tienda.Model.User;
import iw_part2.tienda.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/auth/")
public class LoginRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupRepository groupRepository;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/register")
    public ResponseEntity save(@RequestParam("userName") String userName, @RequestParam("userEmail") String userEmail, @RequestParam("userPassword") String userPassword,
                               @RequestParam("userDate") String userDate, @RequestParam("userAddress") String userAddress, @RequestParam(name = "file", required = false)MultipartFile multipartFile) {
        try {
            User user = new User(userEmail);
            Group group = new Group(2L);
            user.setName(userName);
            user.setPassword(userPassword);
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formato.parse(userDate);
            user.setBirthdate(fecha);
            user.setDeliveryAddress(userAddress);
            // No añado el grupo aquí aún, lo añado despues de crearlo
            Collection<Group> grupos = new ArrayList<>();
            grupos.add(groupRepository.findByName("USER").get());
            user.setGroups(grupos);
            String fileName = "";
            if ( multipartFile != null)
                fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "./user-images";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if(!fileName.equals("")) {
                user.setImage(fileName);
                userService.registrar(user);
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new IOException("Could not save the uploaded file: " + fileName);
                }
            } else {
                user.setImage("img.png");
                userService.registrar(user);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(exception);
        }
    }

    @PostMapping(value = "/login")
    public Object login(@RequestBody User user) {

        UserService.LoginStatus loginStatus = userService.login(user.getEmail(), user.getPassword());

        if (loginStatus == UserService.LoginStatus.LOGIN_OK) {
            User usuario = userService.findByEmail(user.getEmail());
            String token = getJWTToken(usuario.getNombre());
            usuario.setToken(token);
            return usuario;
        } else if (loginStatus == UserService.LoginStatus.USER_NOT_FOUND) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        }
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

    @PostMapping(value = "/upload")
    public String fileupload(@RequestParam("file")MultipartFile multipartFile){
        return String.format("file is uploaded succesfully", multipartFile.getOriginalFilename());
    }
}
