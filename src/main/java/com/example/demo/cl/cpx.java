package com.example.demo.cl;

        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class cpx {
    @RequestMapping("/xixi")
    public String xx(){
        return "test";
    }
}
