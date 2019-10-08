import React, { Component } from 'react';
import Footer from './Footer'
import Board from './Board'
import Information from "./Information";

class MainPage extends Component {
    render() {
        return (
            <div id="main-content" className="container-fluid" >
                <div className="jumbotron text-center">
                    <Information />
                    <Board />
                    <Footer />
                </div>
            </div>
        );
    }

}

export default MainPage;
