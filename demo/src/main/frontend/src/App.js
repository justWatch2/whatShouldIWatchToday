import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import axios from "axios";

function App() {
    const [hello, setHello] = useState('');
    useEffect(() => {
        const source = axios.CancelToken.source();
        axios.get('/api/test', {cancelToken: source.token})
            .then((res) => {
                setHello((prev)=>(prev !== res.data? res.data:prev));
            })
            .catch((error)=>{
                if(axios.isCancel(error)) return;
                console.error(error);
            });
        return ()=> source.cancel();
    }, []);
    console.log("렌더링됨!"); // 상태/속성 변경 시마다 호출
    useEffect(() => {
        console.log("마운트됨!"); // 처음 화면에 나타날 때 1번 호출
        return () => console.log("언마운트됨!"); // 컴포넌트가 제거될 때
    }, []);
    return (
        <div className="App container mt-5">
            <div className="alert alert-primary" role="alert">
                백엔드 데이터: {hello}
            </div>
            <h2 className="text-success">Hello World</h2>
        </div>
    );
}


export default App;
