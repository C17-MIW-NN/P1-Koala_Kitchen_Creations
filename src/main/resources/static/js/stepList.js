
    function addStep() {
    const container = document.getElementById("stepsContainer");
    const index = container.children.length;

    const html = `
        <div class="input-group mb-3">
            <input class="form-control" name="recipeSteps[${index}].stepDescription" placeholder="Describe step..." />
            <input type="hidden" name="recipeSteps[${index}].recipeStepId" value="">
            <input type="hidden" name="recipeSteps[${index}].stepNumber" value="">
            <input type="hidden" name="recipeSteps[${index}].recipe" value="">
            <button type="button" class="btn btn-danger" onclick="deleteStep(this)">
                <i class="bi bi-trash-fill"></i>
            </button>
        </div>`;
    container.insertAdjacentHTML("beforeend", html);
    reindexSteps();
}

    function deleteStep(button) {
    button.parentElement.remove();
    reindexSteps();
}

    function reindexSteps() {
    const rows = document.querySelectorAll("#stepsContainer .input-group");
    rows.forEach((row, index) => {
    row.querySelectorAll("input").forEach(input => {
    const field = input.name.substring(input.name.indexOf('.') + 1);
    input.name = `recipeSteps[${index}].${field}`;
});
});
}
