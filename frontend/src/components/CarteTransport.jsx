import './CarteTransport.scss';

export function CarteTransport(props) {
    let { carte, overlay, ws } = props;
    if (!carte) {
        // placeholder
        return <div className="carte-transport placeholder" />;
    }

    let imageName;
    let iconName;
    if (carte.estDouble) {
        imageName = `carte-DOUBLE-${carte.couleur}`;
        iconName = 'DOUBLE';
    } else {
        imageName = `carte-${carte.type}-${carte.couleur}`;
        iconName = carte.type;
    }
    if (carte.ancre) {
        imageName += "-A";
    }
    return <div
        className={`carte-transport`}
        onClick={() => ws.send(carte.nom)}
    >
        <div
            className="image-transport"
            style={{ backgroundImage: `url(images/icone-${iconName}.png), url(images/${imageName}.png)` }}
        />
        {overlay && <div className="overlay" />}
    </div>
}