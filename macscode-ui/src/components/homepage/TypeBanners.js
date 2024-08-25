import React from 'react';
import '../../styles/Home.css';

const TypeBanners = ({selectedType, handleBannerClick}) => {
    return (
        <div className="type-banners">
            <button className={`banner karel ${selectedType === 'karel' ? 'active' : ''}`}
                    onClick={() => handleBannerClick('karel')}>
                Programming Methodology (Karel)
            </button>
            <button className={`banner java ${selectedType === 'java' ? 'active' : ''}`}
                    onClick={() => handleBannerClick('java')}>
                Programming Methodology (Java)
            </button>
            <button className={`banner cpp ${selectedType === 'cpp' ? 'active' : ''}`}
                    onClick={() => handleBannerClick('cpp')}>
                Programming Abstractions
            </button>
        </div>
    );
};

export default TypeBanners;
