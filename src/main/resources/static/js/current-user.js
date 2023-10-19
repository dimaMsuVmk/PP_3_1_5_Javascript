const URLNavbarUser = 'http://localhost:8080/api/currentUser';
const navbarBrandUser = document.getElementById('navbarBrandUser');//Элемент, где будет роль и почта текущего юзера
const tableUserUser = document.getElementById('tableUser');//Элемент, где будет таблица текущего юзера

function getCurrentUser() {
    fetch(URLNavbarUser)
        .then((res) => res.json())
        .then((user) => {

            let rolesStringUser = rolesToStringForUser(user.roles);
            let data = '';

            data += `<tr>
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.email}</td>
            <td>${rolesStringUser}</td>
            </tr>`;
            tableUserUser.innerHTML = data;
            navbarBrandUser.innerHTML = `<span class="align-middle px-2">
                        <span class="fw-bold">${user.email}</span>
                        with roles:
                        <span>${rolesStringUser}</span>
                    </span>`;
        });
}

getCurrentUser()

function rolesToStringForUser(roles) {
    let rolesString = '';
    for (const role of roles) {
        rolesString += (role.name.replace('ROLE_', '') + ', ');
    }
    rolesString = rolesString.substring(0, rolesString.length - 2); // -2, чтобы не показывать последнюю запятую с пробелом
    return rolesString;
}