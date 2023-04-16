import { CarteTransport } from './CarteTransport';
import './Joueur.scss';


function destinationToString(destination) {
    let valeur = String(destination.valeurSimple);
    if (destination.villes.length > 2) {
        valeur += ", " + destination.valeurMax + ", " + destination.penalite;
    }
    return destination.villes.join(" - ") + " (" + valeur + ")";
}


export function Joueur(props) {
    const { joueur, estJoueurCourant, setHighlightedRoutes, ws } = props;
    return (
        <div className={`panneau-joueur ${joueur.couleur} ${estJoueurCourant ? 'actif' : ''}`}>
            <div className="header row">
                <img
                    className="avatar"
                    alt={`avatar ${joueur.couleur}`}
                    src={`images/avatar-${joueur.couleur}.png`}
                />
                <div className="info column">
                    <span className="nom">{joueur.nom}</span>
                    <div className="score">Score: {joueur.score}</div>
                    <div className="wagons">Wagons: {joueur.pionsWagon} ({joueur.pionsWagonReserve})</div>
                    <div className="bateaux">Bateaux: {joueur.pionsBateau} ({joueur.pionsBateauReserve})</div>
                </div>
            </div>
            <div className="secret">
                <div className="destinations column">
                    {joueur.destinationsIncompletes.map((destination, index) =>
                        <div
                            key={index}
                            className="destination"
                            onMouseEnter={() => setHighlightedRoutes(joueur.routesPourDestinations[index])}
                            onMouseLeave={() => setHighlightedRoutes([])}
                        >
                            {destinationToString(destination)}
                        </div>
                    )}
                    {joueur.destinationsCompletes.map((destination, index) =>
                        <div
                            key={index}
                            className="destination complete"
                        >
                            {destinationToString(destination)}
                        </div>
                    )}
                </div>
                <div className="cartes-transport">
                    {joueur.main.map((carte, index) =>
                        <CarteTransport key={index} carte={carte} ws={ws} />
                    )}
                </div>
                <div className="cartes-transport">
                    {joueur.inPlay.map((carte, index) =>
                        <CarteTransport key={index} carte={carte} overlay={true} ws={ws} />
                    )}
                </div>
            </div>
        </div>

    )
}