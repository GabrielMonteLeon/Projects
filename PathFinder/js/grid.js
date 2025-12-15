export class Grid {
    //initializes the start and end nodes to be buttons as well as sets several variables
    constructor(app, rows, cols) {
        this.app = app;
        this.rows = rows;
        this.cols = cols;
        this.cellSize = 0;
        this.grid = [];
        this.container = new PIXI.Container();
        this.app.stage.addChild(this.container);
        this.start = { row: 0, col: 0 };
        this.end = { row: 9, col: 9 };
        this.activeSelection = null;
        this.startNodeGraphic = new PIXI.Graphics();
        this.endNodeGraphic = new PIXI.Graphics();
        this.startNodeGraphic.interactive = true;
        this.endNodeGraphic.interactive = true;
        this.startNodeGraphic.on("pointerdown", () => {
            this.activeSelection = "start";
        });
        this.endNodeGraphic.on("pointerdown", () => {
            this.activeSelection = "end";
        });
        this.app.stage.addChild(this.startNodeGraphic);
        this.app.stage.addChild(this.endNodeGraphic);
    }

    //Calls several helper functions to draw the grid
    draw() {
        this.container.removeChildren();
        this.grid = [];
        for (let r = 0; r < this.rows; r++) {
            let row = [];
            for (let c = 0; c < this.cols; c++) {
                let cell = this.drawCell(r, c);
                row.push(cell);
            }
            this.grid.push(row);
        }
        this.drawGridLines();
        this.app.stage.setChildIndex(this.startNodeGraphic, this.app.stage.children.length - 1);
        this.app.stage.setChildIndex(this.endNodeGraphic, this.app.stage.children.length - 1);
        this.drawStartEndNodes();
    }

    //draws the lines for the grid
    drawGridLines() {
        const g = new PIXI.Graphics();
        const width = this.cols * this.cellSize;
        const height = this.rows * this.cellSize;
        for (let r = 0; r <= this.rows; r++) {
            const y = r * this.cellSize;
            g.moveTo(0, y);
            g.lineTo(width, y);
        }
        for (let c = 0; c <= this.cols; c++) {
            const x = c * this.cellSize;
            g.moveTo(x, 0);
            g.lineTo(x, height);
        }
        g.stroke({
            width: 1,
            color: 0x000000,
            pixelLine: true,
        });
        this.container.addChild(g);
    }

    //draws the start and end nodes
    drawStartEndNodes() {
        const sx = this.start.col * this.cellSize;
        const sy = this.start.row * this.cellSize;
        const ex = this.end.col * this.cellSize;
        const ey = this.end.row * this.cellSize;
        this.startNodeGraphic.clear();
        this.startNodeGraphic.x = sx;
        this.startNodeGraphic.y = sy;
        this.startNodeGraphic.beginFill(0x0000ff);
        this.startNodeGraphic.drawRect(2, 2, this.cellSize - 4, this.cellSize - 4);
        this.startNodeGraphic.endFill();
        this.endNodeGraphic.clear();
        this.endNodeGraphic.x = ex;
        this.endNodeGraphic.y = ey;
        this.endNodeGraphic.beginFill(0xff0000);
        this.endNodeGraphic.drawRect(2, 2, this.cellSize - 4, this.cellSize - 4);
        this.endNodeGraphic.endFill();
    }

    //draws a singular cell
    drawCell(r, c) {
        const canvasSize = Math.min(this.app.renderer.width, this.app.renderer.height);
        this.cellSize = canvasSize / this.cols;
        let g = new PIXI.Graphics();
        let x = c * this.cellSize;
        let y = r * this.cellSize;
        g.beginFill(0xffffff);
        g.drawRect(x, y, this.cellSize, this.cellSize);
        g.endFill();
        g.interactive = true;
        g.on("pointerdown", () => {
            if (this.pathFinished) {
                this.clearSearch();
                return;
            }
            if (this.activeSelection === "start") {
                this.start.row = r;
                this.start.col = c;
                this.drawStartEndNodes();
                this.activeSelection = null;
                return;
            }
            if (this.activeSelection === "end") {
                this.end.row = r;
                this.end.col = c;
                this.drawStartEndNodes();
                this.activeSelection = null;
                return;
            }
            this.toggleWall(r, c);
        });
        this.container.addChild(g);
        return { graphic: g, wall: false };
    }

    //resizes the grid based on new rows and cols and moves start and end node to the corners
    resize(newRows, newCols) {
        document.querySelector("#status-message").innerHTML="";
        this.pathFinished = false;
        this.rows = newRows;
        this.cols = newCols;
        const canvasSize = Math.min(this.app.renderer.width, this.app.renderer.height);
        this.cellSize = canvasSize / this.cols;
        this.start = { row: 0, col: 0 };
        this.end = { row: newRows - 1, col: newCols - 1 };
        this.draw();
    }

    //changes whether the cell at row r and col c are empty or a wall
    toggleWall(r, c) {
        if((this.start.row==r&&this.start.col==c) ||(this.end.row==r&&this.end.col==c)){return;} //while it theoretically shouldn't be possible you can click on the edges of the boxes around the start and end nodes so wanted to prevent that
        let cell = this.grid[r][c];
        cell.wall = !cell.wall;
        let x = c * this.cellSize;
        let y = r * this.cellSize;
        cell.graphic.clear();
        let color = cell.wall ? 0x000000 : 0xffffff;
        cell.graphic.beginFill(color);
        cell.graphic.drawRect(x, y, this.cellSize, this.cellSize);
        cell.graphic.endFill();
    }

    //clears the previous path search
    clearSearch() {
        document.querySelector("#status-message").innerHTML="";
        this.pathFinished = false;
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                const cell = this.grid[r][c];
                if (!cell.wall) {
                    const g = cell.graphic;
                    const x = c * this.cellSize;
                    const y = r * this.cellSize;
                    g.clear();
                    g.beginFill(0xffffff);
                    g.drawRect(x, y, this.cellSize, this.cellSize);
                    g.endFill();
                }
            }
        }
        this.drawStartEndNodes();
    }
}
