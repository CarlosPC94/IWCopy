package iw_part2.tienda.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api")
public class ImagesController {
    @GetMapping(value="/image")
    public String verImagen() {
        return "imagenes";
    }
}
