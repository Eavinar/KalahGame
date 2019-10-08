import React, { Component } from 'react'

class Information extends Component {
    render() {
        return (
            <section id='information'>
                <h2 className="display-4"><b>Welcome to the game!</b></h2>
                <p className="lead">Please ask your opponent to join the game. In order to do it new window can be opened either in
                    incognito mode or in different browser</p>
                <hr className="my-4" />
            </section>
        )
    }
}

export default Information
