# Ghid de utilizare: AuthController & StaffController

Acest document explică modul de utilizare a claselor `AuthController` și `StaffController` pentru autentificare și gestionarea personalului (staff) în aplicația restaurantului.

---

## 1. Instanțierea repository-urilor și controllerelor

```java
import grupa.unu.restaurant.repository.ManagerRepository;
import grupa.unu.restaurant.repository.StaffRepository;
import grupa.unu.restaurant.controller.AuthController;
import grupa.unu.restaurant.controller.StaffController;

// Instanțiere repository-uri
ManagerRepository managerRepo = new ManagerRepository();
StaffRepository staffRepo = new StaffRepository();

// Instanțiere controllere
AuthController authController = new AuthController(managerRepo, staffRepo);
StaffController staffController = new StaffController(staffRepo);
```

---

## 2. Autentificare utilizator (manager sau staff)

```java
String username = "admin@restaurant.null";
String password = "adminRestaurantMagic12";

AuthController.AuthResult result = authController.authenticate(username, password);

switch (result.getType()) {
    case MANAGER:
        // Utilizatorul este manager
        System.out.println("Autentificat ca manager: " + result.getManager().getUsername());
        break;
    case STAFF:
        // Utilizatorul este staff
        System.out.println("Autentificat ca staff: " + result.getStaff().getUsername());
        break;
    case NONE:
        // Autentificare eșuată
        System.out.println("Autentificare eșuată!");
        break;
}
```

---

## 3. Gestionare staff (doar pentru manager)

### a) Listare staff

```java
List<Staff> staffList = staffController.getAllStaff();
for (Staff staff : staffList) {
    System.out.println(staff.getId() + " | " + staff.getUsername() + " | " + staff.getRole());
}
```

### b) Adăugare staff

```java
boolean added = staffController.addStaff("usernou", "parola123", "chelner");
if (added) {
    System.out.println("Staff adăugat cu succes!");
} else {
    System.out.println("Eroare la adăugare staff!");
}
```

### c) Ștergere staff

```java
int staffId = 3; // ID-ul staff-ului de șters
boolean deleted = staffController.deleteStaffById(staffId);
if (deleted) {
    System.out.println("Staff șters cu succes!");
} else {
    System.out.println("Eroare la ștergere staff!");
}
```

---

## Observații

- **Autentificarea** returnează tipul de utilizator și obiectul asociat (Manager sau Staff).
- **Gestionarea staff** (adăugare/ștergere) trebuie făcută doar de către manager, după autentificare.
- Pentru orice operațiune, tratați excepțiile `SQLException` acolo unde este cazul.

---

Dacă aveți întrebări suplimentare, contactați membrul responsabil de modulul 1 (autentificare și gestionare utilizatori).