package mil.nga.giat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@RestController
class GreetingController {

    @XmlRootElement
    static class Greeting {
        Greeting() {}

        @XmlElement
        public String getContent() {
            return "Hello World!";
        }
    }

    @GetMapping("/")
    Greeting index() {
        return new Greeting();
    }
}