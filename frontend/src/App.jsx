import { useEffect, useState } from 'react';
import './App.scss';
import { CartesVisibles } from './components/CartesVisibles';
import { Joueur } from './components/Joueur';
import { Log } from './components/Log';
import { Piles } from './components/Piles';
import { Plateau } from './components/Plateau';
import { Prompt } from './components/Prompt';

function App() {
  const [ws, setWs] = useState(undefined);
  const [data, setData] = useState(undefined);
  const [highlightedRoutes, setHighlightedRoutes] = useState([]);

  useEffect(() => {
    if (ws === undefined) {
      let hostname = window.location.hostname;
      if (hostname === "") {
        hostname = "localhost";
      }
      const websocket = new WebSocket(`ws://${hostname}:3232`);
      websocket.onmessage = function (event) {
        const receivedData = JSON.parse(event.data);
        console.log(receivedData);
        setData(receivedData);
      };
      websocket._send = websocket.send;
      websocket.send = function (message) {
        console.log("Message envoyé: " + message);
        websocket._send(message);
      };
      setWs(websocket);
    }
  }, [ws]);

  if (data === undefined) {
    return (<div id="main">
      <p>La connexion avec le serveur n'a pas pu être établie.</p>
      <p>Démarrez le serveur et rechargez la page.</p>
    </div>)
  }

  return (
    <main>
      <div id="main" className='row'>
        <div id="left-side" className='column'>
          <Plateau ws={ws} joueurs={data.joueurs} highlightedRoutes={highlightedRoutes} />
          <Prompt joueur={data.joueurs[data.joueurCourant]} instruction={data.instruction} boutons={data.boutons} ws={ws} />
          <CartesVisibles cartes={data.cartesTransportVisibles} ws={ws} />
          <div className='row'>
            <Log logLines={data.log} />
            <Piles
              piocheWagon={data.piocheWagon}
              piocheBateau={data.piocheBateau}
              nbDestinations={data.nbDestinations}
              ws={ws}
            />
          </div>
        </div>
        <div className='column'>
          <div className="joueurs">
            {data.joueurs.map((joueur, index) =>
              <Joueur
                key={index}
                joueur={joueur}
                estJoueurCourant={data.joueurCourant === index}
                setHighlightedRoutes={setHighlightedRoutes}
                ws={ws}
              />
            )}
          </div>
        </div>
      </div>
    </main>
  )
}

export default App
