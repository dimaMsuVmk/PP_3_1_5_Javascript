const URLNavbarAdmin = 'http://localhost:8080/api/currentUser';
const navbarBrandAdmin = document.getElementById('navbarBrandAdmin');//Элемент, где будет роль и почта текущего юзера
const tableUserAdmin = document.getElementById('tableAdmin');//Элемент, где будет таблица текущего юзера

function getCurrentAdmin() {
    fetch(URLNavbarAdmin)
        .then((res) => res.json())
        .then((userAdmin) => {

            let rolesStringAdmin = rolesToStringForAdmin(userAdmin.roles);
            let data = '';

            data += `<tr>
            <td>${userAdmin.id}</td>
            <td>${userAdmin.firstName}</td>
            <td>${userAdmin.lastName}</td>
            <td>${userAdmin.email}</td>
            <td>${rolesStringAdmin}</td>
            </tr>`;
            tableUserAdmin.innerHTML = data;
            navbarBrandAdmin.innerHTML = `<span class="align-middle px-2">
                        <span class="fw-bold">${userAdmin.email}</span>
                        with roles:
                        <span>${rolesStringAdmin}</span>
                    </span>`;
        });
}

getCurrentAdmin()

function rolesToStringForAdmin(roles) {
        let rolesString = '';
        for (const role of roles) {
            rolesString += (role.name.replace('ROLE_', '') + ', ');
        }
        rolesString = rolesString.substring(0, rolesString.length - 2); // -2, чтобы не показывать последнюю запятую с пробелом
        return rolesString;
    }