import { Grid } from './grid.js';
import { Pathfinder } from './pathfinder.js';

let app;
let grid;

//initializes the pixi
async function initPixi() {
    app = new PIXI.Application();
    await app.init({
        width: 500,
        height: 500,
        background: '#dddddd',
        antialias: true,
        eventFeatures: {
            move: true,
            globalMove: true,
            up: true,
            globalUp: true
        }
    });
    document.querySelector("#pixi-container").appendChild(app.canvas);
}

//initilizes the pathfinding grid
function initGrid() {
    grid = new Grid(app, 10, 10);
    grid.draw();
}

//Gives the buttons and selectors their event listeners
function initUI() {
    document.querySelector("#grid-size").addEventListener("change", (e) => {
        let size = parseInt(e.target.value);
        grid.resize(size, size);
    });
    document.querySelector("#run-btn").addEventListener("click", () => {
        let alg = document.getElementById("algorithm-select").value;
        let pf = new Pathfinder(grid, alg);
        pf.run();
    });
    document.querySelector("#clear-btn").addEventListener("click", () => {
        grid.clearSearch();
    });
    document.querySelector("#reset-btn").addEventListener("click", () => {
        let size = parseInt(document.querySelector("#grid-size").value);
        grid.resize(size, size);
    });
}

//initializes grid and ui after the pixi is initialized
(async function start() {
    await initPixi();
    initGrid();
    initUI();
})();
