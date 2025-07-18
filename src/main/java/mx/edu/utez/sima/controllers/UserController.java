package mx.edu.utez.sima.controllers;

import mx.edu.utez.sima.modules.user.BeanUser;
import mx.edu.utez.sima.services.UserService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<APIResponse> createUser(@RequestBody BeanUser user) {
        return userService.createUser(user);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<APIResponse> getUserByUuid(@PathVariable String uuid) {
        return userService.getUserByUuid(uuid);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<APIResponse> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id, @RequestBody BeanUser userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<APIResponse> toggleUserStatus(@PathVariable Long id) {
        return userService.toggleUserStatus(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<APIResponse> assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return userService.assignRole(userId, roleId);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<APIResponse> getUsersByRole(@PathVariable Long roleId) {
        return userService.getUsersByRole(roleId);
    }

    @GetMapping("/available-managers")
    public ResponseEntity<APIResponse> getAvailableWarehouseManagers() {
        return userService.getAvailableWarehouseManagers();
    }

    @GetMapping("/active")
    public ResponseEntity<APIResponse> getActiveUsers() {
        return userService.getActiveUsers();
    }
}