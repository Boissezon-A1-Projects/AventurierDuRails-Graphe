import './Prompt.scss';
import parse from 'html-react-parser';

export function Prompt(props) {
    let { joueur, instruction, boutons, ws } = props;
    return <div id="prompt">
        <div id="instruction">
            <strong>{joueur.nom}: </strong>{parse(instruction)}
            <div id="boutons">{boutons.map((bouton, index) => 
                <button key={index} onClick={() => ws.send(bouton.valeur)}>{bouton.label}</button>
            )}</div>
        </div>
        <div id="prompt-buttons">
            <input id="console-input" type="text" onKeyDown={(e) => {
                if (e.key !== "Enter") return;
                const input = document.getElementById("console-input");
                ws.send(input.value);
                input.value = "";
            }} />
            <button onClick={() => ws.send("")}>Passer</button>
        </div>
    </div>;
}