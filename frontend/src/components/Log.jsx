import './Log.scss';
import parse from 'html-react-parser';
import { useEffect, useRef } from 'react';

export function Log(props) {
    const innerLogRef = useRef(null);
    useEffect(() => {
        innerLogRef.current.scrollTop = innerLogRef.current.scrollHeight;
    }, [props.logLines]);

    const { logLines } = props;
    return <div id="log">
        <div id="inner-log" ref={innerLogRef}>
            {logLines.map((line, index) => <pre key={index}>{parse(line)}</pre>)}
        </div>
    </div>;
}