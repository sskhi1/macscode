import React from 'react';
import '../styles/Karel.css';

function KarelWorld({ width, height, karelX, karelY, karelDirection, grid, borders, results, testNum }) {
    // Calculate cell size based on screen width
    const cellSize = Math.min(window.innerWidth/ width * 0.35, 70);

    // Grid style with calculated cell size
    const gridStyle = {
        gridTemplateColumns: `repeat(${width}, ${cellSize}px)`,
        gridTemplateRows: `repeat(${height}, ${cellSize}px)`,
    };

    const renderCell = (x, y) => {
        const adjustedY = height - 1 - y; // Adjust for bottom-left corner system
        const isKarel = x === karelX && adjustedY === karelY;
        const cellContent = grid[y][x]; // Beepers are shown as per the original y-coordinate (top-left system)
        const walls = borders[y][x]; // Walls are based on bottom-left corner system

        return (
            <div key={`${x}-${y}`} className="cell" style={{ width: cellSize, height: cellSize }}>
                {isKarel && <div className={`karel-robot karel-${karelDirection}`} />}
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
                <div>{results[testNum-1].additionalInfo}</div>
            </div>
        </div>
    );
}

export default KarelWorld;
