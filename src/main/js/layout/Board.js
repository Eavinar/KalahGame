import React, { Component } from 'react'
import TableColumn from './TableColumn'
import axios from 'axios'

class Board extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: "",
            isConnected: false,
            isDisconnected: true,
            boardDetails: { 'pits': [], 'stores': [{}, {}] }
        };

        this.handleChange = this.handleChange.bind(this);
        this.connect = this.connect.bind(this);
        this.disconnect = this.disconnect.bind(this);
        this.sendUserMoveDetails = this.sendUserMoveDetails.bind(this);
        this.getGameStatus = this.getGameStatus.bind(this);
    }

    interval = 0;

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    handleChange({ target }) {
        this.setState({
            [target.name]: target.value
        })
    }

    connect(e) {
        e.preventDefault();
        let user = this.state.user;
        const response = axios.post(
            'http://localhost:8080/react/connect',
            {
                'name': user
            },
            { headers: { 'Content-Type': 'application/json' } }
        ).then((response) => {
            this.setState({
                isConnected: !this.state.isConnected,
                isDisconnected: !this.state.isDisconnected,
            })
            this.getGameStatus();
        }).then(() => this.getGameStatus())
            .catch((error) => {
                if (error.response) {
                    alert(error.response.data.message);
                }
            })
    }

    disconnect(e) {
        e.preventDefault();
        let user = this.state.user;
        const response = axios.post(
            'http://localhost:8080/react/disconnect',
            {
                'name': user
            },
            { headers: { 'Content-Type': 'application/json' } }
        ).then((response) => {
            this.setState({
                isConnected: !this.state.isConnected,
                isDisconnected: !this.state.isDisconnected,
            })
            window.location.reload();
        }).catch((error) => {
            if (error.response) {
                alert(error.response.data.message);
            }
        });
    }

    getGameStatus() {
        this.interval = setInterval(() => {
            axios.get('http://localhost:8080/react/getGameStatus')
                .then((response) => {
                    if (['GOING_ON', 'USER1_WINS', 'USER2_WINS', 'DRAW'].indexOf(response.data.gameStatus.toUpperCase()) > -1) {
                        console.log(2);
                        this.setState({
                            isConnected: true,
                            isDisconnected: false,
                            boardDetails: response.data
                        });

                        if (['USER1_WINS', 'USER2_WINS', 'DRAW'].indexOf(response.data.gameStatus.toUpperCase()) > -1) {
                            alert(response.data.message);
                            window.location.reload();
                        }
                    }
                }).catch((error) => {
                    if (error.response) {
                        alert(error.response.data.message);
                    }
                });
        }, 3000);
    }

    sendUserMoveDetails(id, stones) {
        let user = this.state.user;
        const response = axios.post(
            'http://localhost:8080/react/move',
            {
                'user': {
                    'name': user
                },
                'stepId': id
            },
            { headers: { 'Content-Type': 'application/json' } }
        ).then((response) => {
            this.setState({
                boardDetails: response.data
            });
        }).catch((error) => {
            if (error.response) {
                alert(error.response.data.message);
            }
        })
    }

    render() {
        return (
            <section id="board">
                <div className="row">
                    <div className="col-md-12 column-in-center">
                        <form className="form-inline">
                            <div className="form-group">
                                <input type="text" id="name" name="user" value={this.state.user} className="form-control" placeholder="Username..." onChange={this.handleChange} disabled={this.state.isConnected} />
                            </div>

                            <div className="form-group">
                                <button id="connect" className="btn btn-default" type="submit" onClick={this.connect} disabled={this.state.isConnected}>Connect</button>
                                <button id="disconnect" className="btn btn-default" type="submit" onClick={this.disconnect} disabled={this.state.isDisconnected}>Disconnect
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
                <div className="row mt-50">
                    <div className="col-md-12 table-responsive">
                        <table id="gameBoard" className="table-center" style={{ display: 'stonesCount' in this.state.boardDetails.stores[0] ? 'block' : 'none' }}>
                            <tbody>
                                <tr>
                                    <td rowSpan="2" className="schema-1 board store">
                                        {this.state.boardDetails.stores[0].stonesCount}
                                    </td>
                                    {this.state.boardDetails.pits.slice(0, 6).reverse()
                                        .map((data, i) => (
                                            <TableColumn
                                                key={i}
                                                id={6 - i}
                                                class={"schema-1 board"}
                                                stones={data.stonesCount}
                                                onHeaderClick={this.sendUserMoveDetails}
                                            />
                                        ))}
                                    <td rowSpan="2" className="schema-2 board store">
                                        {this.state.boardDetails.stores[1].stonesCount}
                                    </td>
                                </tr>
                                <tr>
                                    {this.state.boardDetails.pits.slice(6, 12)
                                        .map((data, i) => (
                                            <TableColumn
                                                key={i}
                                                id={i + 7}
                                                class={"schema-2 board"}
                                                stones={data.stonesCount}
                                                onHeaderClick={this.sendUserMoveDetails}
                                            />
                                        ))}
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        )
    }
}

export default Board;
