import React from 'react';
import '../../styles/Home.css';

const TypeBanners = ({selectedType, handleBannerClick}) => {
    return (
        <div className="type-banners">
            <button className={`banner java ${selectedType === 'java' ? 'active' : ''}`}
                    onClick={() => handleBannerClick('java')}>
                JAVA
            </button>
            <button className={`banner cpp ${selectedType === 'cpp' ? 'active' : ''}`}
                    onClick={() => handleBannerClick('cpp')}>
                C++
            </button>
            <button className={`banner karel ${selectedType === 'karel' ? 'active' : ''}`}
                    onClick={() => handleBannerClick('karel')}>
                KAREL
            </button>
        </div>
    );
};

export default TypeBanners;
