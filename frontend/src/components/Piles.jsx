import './Piles.scss';
import { CarteTransport } from "./CarteTransport";

export function Piles(props) {
    const {
        piocheBateau,
        piocheWagon,
        nbDestinations,
        ws,
    } = props;

    return <div id="piles" className="column">
        <div id="pioches" className='row'>
            <div className='row'>
                <div className="pile-pioche column">
                    <span>Cartes wagon ({piocheWagon.pioche})</span>
                    <img
                        className="carte-transport"
                        alt="pile cartes wagon"
                        src="images/dos-WAGON.png"
                        onClick={() => ws.send("WAGON")}
                    />
                </div>
                <div className="defausse column">
                    <span> Défausse wagon({piocheWagon.defausse.length})</span>
                    <div className='defausse-cartes'>
                        {piocheWagon.defausse.length > 0 ?
                            piocheWagon.defausse.slice(-10).map((carte, index) =>
                                <CarteTransport key={index} carte={carte} ws={ws} />
                            )
                            : <CarteTransport carte={null} />
                        }
                    </div>
                </div>
            </div>
            <div className='row'>
                <div className="pile-pioche column">
                    <span>Cartes bateau ({piocheBateau.pioche})</span>
                    <img
                        className="carte-transport"
                        alt="pile cartes bateau"
                        src="images/dos-BATEAU.png"
                        onClick={() => ws.send("BATEAU")}
                    />
                </div>
                <div className="defausse column">
                    <span> Défausse bateau({piocheBateau.defausse.length})</span>
                    <div className='defausse-cartes'>
                        {piocheBateau.defausse.length > 0 ?
                            piocheBateau.defausse.slice(-10).map((carte, index) =>
                                <CarteTransport key={index} carte={carte} ws={ws} />
                            )
                            : <CarteTransport carte={null} />
                        }
                    </div>
                </div>
            </div>
        </div>
        <div className='pile-pioche column'>
            <span>Destinations ({nbDestinations})</span>
            <div id="pile-destinations">
                <img
                    className="carte-transport"
                    alt="destinations"
                    src="images/destinations.png"
                    onClick={() => ws.send("DESTINATION")}
                />
            </div>
        </div>
    </div>;
}