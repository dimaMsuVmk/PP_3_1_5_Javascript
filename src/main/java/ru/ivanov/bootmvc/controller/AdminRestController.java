package ru.ivanov.bootmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ivanov.bootmvc.model.Role;
import ru.ivanov.bootmvc.model.User;
import ru.ivanov.bootmvc.dao.RoleDao;
import ru.ivanov.bootmvc.service.RoleService;
import ru.ivanov.bootmvc.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * RestController – это Controller, который управляет REST запросами и ответами.
 * Такие Спринг-приложении, которые принимают http-запросы и не реализуют представления,
 * а отдают сырые данные в формате JSON (в 99% случаев, т.к. это самый распространенный формат),
 * называются REST API.
 * По принятому стандарту url любого запроса в REST API должно начинаться с /api,
 * поэтому всему rest-контроллеру ставим такой url
 * Теперь, когда со стороны клиента, т.е. браузера, будет приходить запрос, содержащий в url "/api",
 * то Спринг с помощью функционала проекта Jackson будет конвертировать данные в JSON-формат
 * и в теле http-response будет передан JSON, который отобразится в браузере.
 * <p>
 * Чтобы получать о запросах и ответа больше инфы, есть разные проги. Одна из них - Postman.
 * Т.е. в качестве клиента будет не браузер, а Postman.
 */
@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/currentUser")
    public ResponseEntity<User> showInfoUser(Principal principal) {
        return ResponseEntity.ok(userService.findByUserName(principal.getName()));
    }
    @GetMapping(value = "/roles")
    public ResponseEntity<Collection<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/roles/{id}")
    public ResponseEntity<Collection<Role>> getRole(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id).getRoles(), HttpStatus.OK);
    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователя с ID = " + id + " нет в БД");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/admin")
    public ResponseEntity<User> addNewUser(@RequestBody User newUser) {
        userService.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }
    @PatchMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User userFromWebPage, @PathVariable("id") Long id) {
        userService.updateUser(userFromWebPage);
        return new ResponseEntity<>(userFromWebPage, HttpStatus.OK);
    }
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if(userService.removeUserById(id) == false){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User has been deleted");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}