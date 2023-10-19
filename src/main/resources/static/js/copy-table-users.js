const URLEdit = "http://localhost:8080/api/admin";
const datatableElem = document.getElementById("bodyUsers");
let tableStr = '';
fillTable();
function fillTable() {
    fetch(URLEdit)
        .then(response => response.json())
        .then(arrayUsers => arrayUsers.forEach((user) => {
            tableStr += `<tr>
                          <td>${user.id}</td>
                          <td>${user.firstName}</td>
                          <td>${user.lastName}</td>
                          <td>${user.email}</td>
                          <td>${rolesToString(user.roles)}</td>
                          <td>
                              <button type="button"
                              class="btn btn-info"
                              data-bs-toogle="modal"
                              data-bs-target="#editModal"
                              onclick="editModal(${user.id})">Edit
                              </button>
                          </td>
                          <td>
                              <button type="button" 
                              class="btn btn-danger" 
                              data-toggle="modal" 
                              data-target="#deleteModal" 
                              onclick="deleteModal(${user.id})">Delete
                              </button>
                          </td>
                     </tr>`
        }))
        .catch(error => console.log(error));

    datatableElem.innerHTML = tableStr;
}

function rolesToString(roles) {
    let rolesString = '';
    for (const role of roles) {
        rolesString += (role.name.replace('ROLE_', '') + ', ');
    }
    rolesString = rolesString.substring(0, rolesString.length - 2); // -2, чтобы не показывать последнюю запятую с пробелом
    return rolesString;
}