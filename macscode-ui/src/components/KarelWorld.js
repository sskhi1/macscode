import React, { useEffect, useState } from 'react';
import '../styles/Karel.css';

function KarelWorld({ width, height, karelX, karelY, karelDirection, grid, borders, results, testNum }) {
    const [currentX, setCurrentX] = useState(karelX);
    const [currentY, setCurrentY] = useState(karelY);
    const [currentDirection, setCurrentDirection] = useState(karelDirection);
    const [currentGrid, setCurrentGrid] = useState([...grid]);

    const instructions = results[testNum - 1]?.instructions || [];

    // Calculate cell size based on screen width
    const cellSize = Math.min(window.innerWidth / width * 0.35, 70);

    // Grid style with calculated cell size
    const gridStyle = {
        gridTemplateColumns: `repeat(${width}, ${cellSize}px)`,
        gridTemplateRows: `repeat(${height}, ${cellSize}px)`,
    };

    useEffect(() => {
        if (instructions.length > 0) {
            let i = 0;

            const interval = setInterval(() => {
                if (i < instructions.length) {
                    executeInstruction(instructions[i]);
                    i++;
                } else {
                    clearInterval(interval);
                }
            }, 500); // Adjust the speed of animation here (500ms for each step)

            return () => clearInterval(interval);
        }
    }, [instructions]); // Dependency array watches for changes in instructions


    const executeInstruction = (instruction) => {
        switch (instruction) {
            case 'move':
                moveForward();
                break;
            case 'turnLeft':
                turnLeft();
                break;
            case 'turnRight':
                turnRight();
                break;
            case 'turnAround':
                turnAround();
                break;
            case 'pickBeeper':
                pickBeeper();
                break;
            case 'putBeeper':
                putBeeper();
                break;
            default:
                break;
        }
    };

    const moveForward = () => {
        let newX = currentX;
        let newY = currentY;

        switch (currentDirection) {
            case 0: // North
                newY = currentY + 1 < height ? currentY + 1 : currentY;
                break;
            case 1: // East
                newX = currentX + 1 < width ? currentX + 1 : currentX;
                break;
            case 2: // South
                newY = currentY - 1 >= 0 ? currentY - 1 : currentY;
                break;
            case 3: // West
                newX = currentX - 1 >= 0 ? currentX - 1 : currentX;
                break;
            default:
                break;
        }

        setCurrentX(newX);
        setCurrentY(newY);
    };

    const turnLeft = () => {
        setCurrentDirection((currentDirection + 3) % 4);
    };

    const turnRight = () => {
        setCurrentDirection((currentDirection + 1) % 4);
    };

    const turnAround = () => {
        setCurrentDirection((currentDirection + 2) % 4);
    };

    const pickBeeper = () => {
        const updatedGrid = [...currentGrid];
        if (updatedGrid[height - 1 - currentY][currentX] > 0) {
            updatedGrid[height - 1 - currentY][currentX] -= 1;
            setCurrentGrid(updatedGrid);
        }
    };

    const putBeeper = () => {
        const updatedGrid = [...currentGrid];
        updatedGrid[height - 1 - currentY][currentX] += 1;
        setCurrentGrid(updatedGrid);
    };

    const renderCell = (x, y) => {
        const adjustedY = height - 1 - y;
        const isKarel = x === currentX && adjustedY === currentY;
        const cellContent = currentGrid[y][x];
        const walls = borders[y][x];

        return (
            <div key={`${x}-${y}`} className="cell" style={{ width: cellSize, height: cellSize }}>
                {isKarel && <div className={`karel-robot karel-${currentDirection}`} />}
                {cellContent > 0 && <div className="beeper"><div className="beeper-content">
                    {cellContent}
                </div></div>}
                {walls[0] && <div className="wall wall-north" />}
                {walls[1] && <div className="wall wall-east" />}
                {walls[2] && <div className="wall wall-south" />}
                {walls[3] && <div className="wall wall-west" />}
            </div>
        );
    };

    return (
        <div className="karel-world-container">
            <div className="karel-world" style={gridStyle}>
                {[...Array(height)].map((_, y) =>
                    [...Array(width)].map((_, x) => renderCell(x, y))
                )}
            </div>
        </div>
    );
}

export default KarelWorld;
