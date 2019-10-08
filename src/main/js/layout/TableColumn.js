import React, {Component} from 'react';

class TableColumn extends Component{

    handleClick = () => {
        this.props.onHeaderClick(this.props.id, this.props.stones);
    }

    render(){
        return(
            <td data-id={this.props.id} data-stones={this.props.stones} className={this.props.class} onClick={this.handleClick}>                
                {this.props.stones}
            </td>
        );
    }
}

export default TableColumn;
