import React, { Component } from 'react';
import { Alert } from 'react-bootstrap' 
import { createAlert, removeAlert } from '../../actions/alert';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSmileWink } from '@fortawesome/free-solid-svg-icons'
import { connect } from 'react-redux';

class Alerts extends Component {

	removeAlertsAfterMs(alerts, ms) {
        alerts.map((alert) => {
            setTimeout(() => {
                this.props.removeAlert(alert.id);
             }, ms)
        });
	}

    render() { 
        let alertHtml = this.props.alerts.list.map((alert) => {
            return 	<Alert 
            key={alert.id} variant={alert.variant}>
                <FontAwesomeIcon icon={faSmileWink} />
                <Alert.Link href="#">
                    {alert.message}
                </Alert.Link>
                </Alert>
        })
        this.removeAlertsAfterMs(this.props.alerts.list, 2000);

        return (
            <div style={{ 
                offset: "500px",
                position: "absolute",
                width: "500px",
                zIndex: 9999,
                left: "100%",
                transform: "translate(-510px, 10px)"}}>
				{alertHtml}
			</div>
        );
    }
}

const mapStateToProps = (state) => {
	return {
		alerts: state.alerts
	};
}

export default connect(mapStateToProps, {createAlert, removeAlert})(Alerts);