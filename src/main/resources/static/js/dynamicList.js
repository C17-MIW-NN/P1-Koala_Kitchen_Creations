// Author: Josse Muller
// Utility functions to add and remove rows from a dynamically generated Thymeleaf form

function deleteListRow(btn) {
    let listContainer = btn.closest("[data-list-container]");
    if (listContainer.querySelectorAll("[data-list-row]").length === 1) {
        console.log("clearing only row");
        clearListRow(btn.closest("[data-list-row]"));
    } else {
        btn.closest("[data-list-row]").remove();
    }
    reindexList(listContainer);

}

function addListRow(containerId) {
    let listContainer = document.getElementById(containerId);
    let templateRow =listContainer.querySelector("[data-list-row]");
    let newRow = templateRow.cloneNode(true);
    clearListRow(newRow);
    listContainer.appendChild(newRow);
    reindexList(listContainer);
}

function reindexList(listContainer) {
    let rows = listContainer.querySelectorAll("[data-list-row]");
    for (const[idx, row] of rows.entries()) {
        for (let input of row.querySelectorAll("input, textarea")) {
            let newId = input.getAttribute("id").replace(/\d+/, idx);
            input.setAttribute("id", newId);
            let newName = input.getAttribute("name").replace(/\d+/, idx)
            input.setAttribute("name", newName);
        }
    }
}

function clearListRow(row) {
    for (let input of row.querySelectorAll("input:not([data-list-keep]), textarea:not([data-list-keep])")) {
        input.value = "";
    }
}
