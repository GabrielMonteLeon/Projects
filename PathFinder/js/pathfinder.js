export class Pathfinder {
    constructor(grid, algorithm) {
        this.grid = grid;
        this.algorithm = algorithm;
    }

    async run() {
        this.grid.pathFinished = false;
        this.grid.clearSearch();
        console.log("Running " + this.algorithm);
        if (this.algorithm === "bfs") {
            await this.runBFS();
        }
        else if (this.algorithm === "astar") {
            await this.runAStar();
        }
        else {
            console.warn("not coded yet");
        }
    }

    //runs breadth first search
    async runBFS() {
        const rows = this.grid.rows;
        const cols = this.grid.cols;
        const start = this.grid.start;
        const end = this.grid.end;
        let queue = [];
        let visited = new Set();
        let parent = {};
        const encode = (r, c) => `${r},${c}`;
        queue.push(start);
        visited.add(encode(start.row, start.col));
        while (queue.length > 0) {
            let current = queue.shift();
            let r = current.row;
            let c = current.col;
            await this.animateVisit(r, c);
            if (r === end.row && c === end.col) {
                new Audio("sounds/correct.mp3").play();
                this.grid.pathFinished = true;
                document.querySelector("#status-message").innerHTML="Path Found";
                await this.animatePath(parent);
                return;
            }
            const dirs = [
                [1, 0], [-1, 0], [0, 1], [0, -1]
            ];
            for (let [dr, dc] of dirs) {
                let nr = r + dr;
                let nc = c + dc;
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (this.grid.grid[nr][nc].wall) continue;
                let key = encode(nr, nc);
                if (!visited.has(key)) {
                    visited.add(key);
                    parent[key] = { r, c };
                    queue.push({ row: nr, col: nc });
                }
            }
        }
        document.querySelector("#status-message").innerHTML="Path Not Found";
        new Audio("sounds/incorrect.mp3").play();
    }

    //runs AStar
    async runAStar() {
        const rows = this.grid.rows;
        const cols = this.grid.cols;
        const start = this.grid.start;
        const end = this.grid.end;

        const encode = (r, c) => `${r},${c}`;
        let openSet = [];
        let gScore = {};
        let parent = {};

        const h = (r, c) => Math.abs(r - end.row) + Math.abs(c - end.col);

        gScore[encode(start.row, start.col)] = 0;
        openSet.push({ r: start.row, c: start.col, f: h(start.row, start.col) });

        let inOpen = new Set([encode(start.row, start.col)]);
        let visited = new Set();

        while (openSet.length > 0) {
            openSet.sort((a, b) => a.f - b.f);
            let current = openSet.shift();
            let key = encode(current.r, current.c);
            inOpen.delete(key);

            if (visited.has(key)) continue;
            visited.add(key);

            await this.animateVisit(current.r, current.c);

            if (current.r === end.row && current.c === end.col) {
                new Audio("sounds/correct.mp3").play();
                this.grid.pathFinished = true;
                document.querySelector("#status-message").innerHTML="Path Found";
                await this.animatePath(parent);
                return;
            }

            const dirs = [
                [1, 0], [-1, 0], [0, 1], [0, -1]
            ];

            for (let [dr, dc] of dirs) {
                let nr = current.r + dr;
                let nc = current.c + dc;
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (this.grid.grid[nr][nc].wall) continue;

                let nKey = encode(nr, nc);
                let tentative = gScore[key] + 1;

                if (gScore[nKey] === undefined || tentative < gScore[nKey]) {
                    gScore[nKey] = tentative;
                    parent[nKey] = { r: current.r, c: current.c };
                    if (!inOpen.has(nKey)) {
                        inOpen.add(nKey);
                        openSet.push({
                            r: nr,
                            c: nc,
                            f: tentative + h(nr, nc)
                        });
                    }
                }
            }
        }
        document.querySelector("#status-message").innerHTML="Path Not Found";
        new Audio("sounds/incorrect.mp3").play();
    }


    //changes each square individially and waits afterwards
    async animateVisit(r, c) {
        if ((r === this.grid.start.row && c === this.grid.start.col) || (r === this.grid.end.row && c === this.grid.end.col)) return;
        const cell = this.grid.grid[r][c].graphic;
        const x = c * this.grid.cellSize;
        const y = r * this.grid.cellSize;
        cell.clear();
        cell.beginFill(0x32a852);
        cell.drawRect(x, y, this.grid.cellSize, this.grid.cellSize);
        cell.endFill();
        return new Promise(resolve => setTimeout(resolve, 10));
    }

    //calls highlight path cell to highlight each square on the path
    async animatePath(parent) {
        const encode = (r, c) => `${r},${c}`;
        let path = [];
        let end = this.grid.end;
        let cur = encode(end.row, end.col);
        while (parent[cur]) {
            let { r, c } = parent[cur];
            path.push({ r, c });
            cur = encode(r, c);
        }
        path.reverse();
        for (let cell of path) {
            await this.highlightPathCell(cell.r, cell.c);
        }
    }

    //highlights the cells at row and col r/c
    async highlightPathCell(r, c) {
        const g = this.grid.grid[r][c].graphic;
        const x = c * this.grid.cellSize;
        const y = r * this.grid.cellSize;
        g.clear();
        g.beginFill(0xffff00);
        g.drawRect(x, y, this.grid.cellSize, this.grid.cellSize);
        g.endFill();
        return new Promise(resolve => setTimeout(resolve, 30));
    }

}
