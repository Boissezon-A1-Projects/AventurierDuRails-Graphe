import { CarteTransport } from "./CarteTransport";
import './CartesVisibles.scss';

export function CartesVisibles(props) {
    const { cartes, ws } = props;
    while (cartes.length < 6) {
        cartes.push(null);
    }

    return <div id="cartes-visibles" className="row">
        {cartes.map((carte, index) =>
            <CarteTransport key={index} carte={carte} ws={ws} />
        )}
    </div>;
}