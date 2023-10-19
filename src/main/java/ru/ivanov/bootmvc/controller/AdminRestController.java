package ru.ivanov.bootmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ivanov.bootmvc.exception.NoSuchUserException;
import ru.ivanov.bootmvc.exception.UserNotCreatedException;
import ru.ivanov.bootmvc.model.Role;
import ru.ivanov.bootmvc.model.User;
import ru.ivanov.bootmvc.dao.RoleDao;
import ru.ivanov.bootmvc.service.UserService;

import javax.validation.Valid;
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
    private final PasswordEncoder passwordEncoder;
    private final RoleDao roleDao;

    @Autowired
    public AdminRestController(UserService userService, PasswordEncoder passwordEncoder, RoleDao roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleRepository;
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
        return new ResponseEntity<>(roleDao.findAll(), HttpStatus.OK);
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
    public ResponseEntity<User> addNewUser(@RequestBody @Valid User newUser, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder info_about_errors = new StringBuilder(); //Создали строку, в которую поместим ошибки
            List<FieldError> fields_of_errors = bindingResult.getFieldErrors(); //Получили список из полей, где произошли ошибки

            for (FieldError error : fields_of_errors) { //Прошлись по ошибкам
                info_about_errors.append(error.getField()) // в строку добавили само поле
                        .append(" - ")
                        .append(error.getDefaultMessage()) //добавили сообщение ошибки
                        .append(";");
            }
            throw new UserNotCreatedException(info_about_errors.toString());
        }
        userService.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }
    @PatchMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User userFromWebPage, @PathVariable("id") Long id) {
        System.out.println("PATCH");
        userService.updateUser(userFromWebPage);
        System.out.println("----------------------------");
        return new ResponseEntity<>(userFromWebPage, HttpStatus.OK);
    }
    @DeleteMapping("/admin /{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if(userService.removeUserById(id) == false){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User has been deleted");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @GetMapping("/admin")
//    public String getUsers(Model model, Principal principal) {
//        if (model.containsAttribute("errorUser"))
//            model.addAttribute("user", model.getAttribute("errorUser"));
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("allRoles", roleRepository.findAll());
//        model.addAttribute("userPrincipal",userService.findByUserName(principal.getName()));
//        return "admin-panel";
//    }

//    @PatchMapping("/admin/{id}")
//    public String updatePerson(@ModelAttribute("user") User updateUser,
//                               @RequestParam String[] selectedRoles,
//                               @RequestParam Long id,
//                               RedirectAttributes redirectAttributes,Model model
//    ) {
//        //??? убрать цикл по ролям и изменить параметр формы name="selectedRoles" на name="roles"
//        // чтобы сразу получать юзера с ролями
//        for (String role : selectedRoles) {
//            if (role.equals("ROLE_USER")) updateUser.getRoles().add(roleDao.getRoleByName("ROLE_USER"));
//            if (role.equals("ROLE_ADMIN")) updateUser.getRoles().add(roleDao.getRoleByName("ROLE_ADMIN"));
//            if (role.equals("ROLE_GUEST")) updateUser.getRoles().add(roleDao.getRoleByName("ROLE_GUEST"));
//        }
//        userService.updateUser(updateUser);
//        return "redirect:/admin";
//    }
//
//    @DeleteMapping("/admin/{id}")
//    public String deletePerson(@PathVariable("id") long id) {
//        userService.removeUserById(id);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/admin")
//    public String create(@ModelAttribute @Validated User user, BindingResult bindingResult,
//                         RedirectAttributes redirectAttributes, @RequestParam(required = false) String[] selectedRoles ) {
//        if(selectedRoles != null) {
//            for (String role : selectedRoles) {
//                if (role.equals("ROLE_USER")) user.getRoles().add(roleDao.getRoleByName("ROLE_USER"));
//                if (role.equals("ROLE_ADMIN")) user.getRoles().add(roleDao.getRoleByName("ROLE_ADMIN"));
//                if (role.equals("ROLE_GUEST")) user.getRoles().add(roleDao.getRoleByName("ROLE_GUEST"));
//            }
//        }
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            redirectAttributes.addFlashAttribute("errorUser", user);
//            return "redirect:/admin";
//        }
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//        userService.save(user);
//        return "redirect:/admin";
//    }
}