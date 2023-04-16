import './Plateau.scss';
import { villesData, routesData } from './plateau_data';

const LONGUEUR_SEGMENT = 50;
const LARGEUR_SEGMENT = 20;
const TAILLE_WAGON = 70;
const TAILLE_PORT = 50;
const RAYON_VILLE = 15;

function toggleTracks() {
    const cache = document.getElementById("cache");
    if (cache.style.display === "") {
        cache.style.display = "block";
    } else {
        cache.style.display = "";
    }
}

export function Plateau(props) {
    const { ws, joueurs, highlightedRoutes } = props;

    return <svg
        id="plateau"
        xmlns="http://www.w3.org/2000/svg"
        xmlnsXlink="http://www.w3.org/1999/xlink"
        viewBox="0 0 1920 1069"
    >
        <rect
            id="cache"
            x="0"
            y="0"
            width="1920"
            height="1069"
        />
        {Object.keys(villesData).map((nom) => {
            const ville = villesData[nom];
            return <g key={nom}>
                <circle
                    className="ville"
                    cx={ville.x}
                    cy={ville.y}
                    r={RAYON_VILLE}
                    onClick={() => ws.send(nom)}
                />
            </g>;
        })}
        {Object.keys(routesData).map((nom) => {
            const route = routesData[nom];
            return <g
                key={route.nom}
                className={`route ${route.couleur} ${route.type}`}
                onClick={() => ws.send(route.nom)}
            >
                {route.segments.map((segment, index) =>
                    <rect
                        key={index}
                        className={highlightedRoutes.includes(nom) ? "segment highlighted" : "segment"}
                        x={segment.x - LONGUEUR_SEGMENT / 2}
                        y={segment.y - LARGEUR_SEGMENT / 2}
                        width={LONGUEUR_SEGMENT}
                        height={LARGEUR_SEGMENT}
                        transform={`rotate(${segment.angle} ${segment.x} ${segment.y})`}
                    />
                )}
            </g>;
        })}
        {joueurs.map((joueur) =>
            joueur.routes.map((nom) => {
                const route = routesData[nom];
                return <g key={nom} className="no-pointer">
                    {route.segments.map((segment, index) =>
                        <g
                            key={index}
                            transform={`translate(${segment.x}, ${segment.y}) rotate(${segment.angle})`}
                            className={`pion`}
                        >
                            <image
                                xlinkHref={`images/image-wagon-${joueur.couleur}.png`}
                                width={TAILLE_WAGON}
                                height={TAILLE_WAGON}
                                transform={`translate(${-TAILLE_WAGON * 0.55}, ${-TAILLE_WAGON / 2})`}
                            />
                        </g>
                    )}
                </g>;
            })
        )}
        {joueurs.map((joueur) =>
            joueur.ports.map((nom) => {
                const ville = villesData[nom];
                return <g key={nom} className="no-pointer" transform={`translate(${ville.x}, ${ville.y})`}>
                    <image
                        xlinkHref="images/gare-shadow.png"
                        width={TAILLE_PORT * 1.05}
                        height={TAILLE_PORT * 1.05}
                        transform={`translate(${-TAILLE_PORT * 0.55}, ${-TAILLE_PORT * 0.75})`}
                    />
                    <image
                        xlinkHref={`images/gare-${joueur.couleur}.png`}
                        width={TAILLE_PORT}
                        height={TAILLE_PORT}
                        transform={`translate(${-TAILLE_PORT * 0.6}, ${-TAILLE_PORT * 0.7})`}
                    />
                </g>;
            })
        )}
        <image id="track-toggle" xlinkHref="images/toggle-button.png" x="0" y="0" width="80" height="80" onClick={toggleTracks} />
    </svg>

}