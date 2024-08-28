import React, {useEffect, useState} from 'react';
import '../styles/Karel.css';

function parseKarelWorld(data) {
    const lines = data.trim().split('\n');

    const width = parseInt(lines[0], 10);
    const height = parseInt(lines[1], 10);

    const karelX = parseInt(lines[2], 10);
    const karelY = parseInt(lines[3], 10);
    const karelDirection = parseInt(lines[4], 10);

    const grid = [];
    for (let i = 0; i < height; i++) {
        const row = lines[5 + i].split(' ').map(Number);
        grid.push(row);
    }

    const borders = Array.from({length: height}, () =>
        Array.from({length: width}, () => [false, false, false, false])
    );

    for (let i = 5 + height; i < lines.length; i++) {
        const [x, y, direction] = lines[i].split(' ').map(Number);
        borders[height - y - 1][x][direction] = true;
    }

    return {
        width,
        height,
        karelX,
        karelY,
        karelDirection,
        grid,
        borders,
    };
}

function ErrorPopup({message, onClose}) {
    return (
        <div className="error-popup">
            <div className="error-popup-content">
                <img
                    src={require('../images/error.png')}
                    alt="Error"
                    className="error-popup-icon"
                />
                <p>{message}</p>
                <button onClick={onClose}>Ok</button>
            </div>
        </div>
    );
}

function KarelWorld({testCaseInput, results, testNum, isDemo}) {
    const {
        width,
        height,
        karelX,
        karelY,
        karelDirection,
        grid,
        borders,
    } = parseKarelWorld(testCaseInput);

    const [currentX, setCurrentX] = useState(karelX);
    const [currentY, setCurrentY] = useState(karelY);
    const [currentDirection, setCurrentDirection] = useState(karelDirection);
    const [currentGrid, setCurrentGrid] = useState([...grid]);
    const [instructionIndex, setInstructionIndex] = useState(0);
    const [instructions, setInstructions] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    const cellSize = Math.min(window.innerWidth / width * 0.35, 70);

    const gridStyle = {
        gridTemplateColumns: `repeat(${width}, ${cellSize}px)`,
        gridTemplateRows: `repeat(${height}, ${cellSize}px)`,
    };

    useEffect(() => {
        setCurrentX(karelX);
        setCurrentY(karelY);
        setCurrentDirection(karelDirection);
        setCurrentGrid([...grid]);
        setInstructionIndex(0);
        setErrorMessage('');

        if (isDemo) {
            const instructions_str = results[testNum - 1]?.additionalInfo || "";
            setInstructions(instructions_str ? instructions_str.split(' ') : []);
        }

    }, [testCaseInput, results]);

    useEffect(() => {
        if (instructionIndex < instructions.length && isDemo && !errorMessage) {
            const timeoutId = setTimeout(() => {
                executeInstruction(instructions[instructionIndex]);
                setInstructionIndex((prevIndex) => prevIndex + 1);
            }, 500);
            return () => clearTimeout(timeoutId);
        }
    }, [instructionIndex, instructions, errorMessage]);

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
        let hitWall = false;

        switch (currentDirection) {
            case 0: // North
                if (currentY + 1 < height && !borders[height - 1 - currentY][currentX][0] && !borders[height - 2 - currentY][currentX][2]) {
                    newY = currentY + 1;
                } else {
                    hitWall = true;
                }
                break;
            case 1: // East
                if (currentX + 1 < width && !borders[height - 1 - currentY][currentX][1] && !borders[height - 1 - currentY][currentX + 1][3]) {
                    newX = currentX + 1;
                } else {
                    hitWall = true;
                }
                break;
            case 2: // South
                if (currentY - 1 >= 0 && !borders[height - 1 - currentY][currentX][2] && !borders[height - currentY][currentX][0]) {
                    newY = currentY - 1;
                } else {
                    hitWall = true;
                }
                break;
            case 3: // West
                if (currentX - 1 >= 0 && !borders[height - 1 - currentY][currentX][3] && !borders[height - 1 - currentY][currentX - 1][1]) {
                    newX = currentX - 1;
                } else {
                    hitWall = true;
                }
                break;
            default:
                break;
        }

        if (hitWall) {
            setErrorMessage('Karel hit a wall!');
        } else {
            setCurrentX(newX);
            setCurrentY(newY);
        }
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
            setErrorMessage('');
        } else {
            setErrorMessage('No beepers to pick up!');
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
            <div key={`${x}-${y}`} className="cell" style={{width: cellSize, height: cellSize}}>
                {isKarel && <div className={`karel-robot karel-${currentDirection}`}/>}
                {cellContent > 0 && (
                    <div className="beeper">
                        <div className="beeper-content">{cellContent}</div>
                    </div>
                )}
                {walls[0] && <div className="wall wall-north"/>}
                {walls[1] && <div className="wall wall-east"/>}
                {walls[2] && <div className="wall wall-south"/>}
                {walls[3] && <div className="wall wall-west"/>}
            </div>
        );
    };

    const resetKarelWorld = () => {
        setCurrentX(karelX);
        setCurrentY(karelY);
        setCurrentDirection(karelDirection);
        setCurrentGrid([...grid]);
        setInstructionIndex(0);
        setInstructions([]);
        setErrorMessage('');
    };

    return (
        <div className="karel-world-container">
            <div className="karel-world" style={gridStyle}>
                {[...Array(height)].map((_, y) =>
                    [...Array(width)].map((_, x) => renderCell(x, y))
                )}
            </div>
            {errorMessage && (
                <ErrorPopup
                    message={errorMessage}
                    onClose={() => setErrorMessage('')}
                />
            )}
            <button onClick={resetKarelWorld} className="restore-button">
                <img
                    src={require('../icons/restore.png')}
                    alt="Restore"
                    className="restore-button-icon"
                />
                Restore Karel's World
            </button>
        </div>
    );
}

export default KarelWorld;