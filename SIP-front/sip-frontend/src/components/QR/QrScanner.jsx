import { useEffect } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";

export default function QrScanner({ onScan }) {

  useEffect(() => {

    const scanner = new Html5QrcodeScanner(
      "qr-reader",
      {
        fps: 10,
        qrbox: { width: 250, height: 250 }
      },
      false
    );

    scanner.render(
      (decodedText) => {
        onScan(decodedText);
      },
      () => {}
    );

    return () => {
      scanner.clear().catch(() => {});
    };

  }, []);

  return <div id="qr-reader" />;
}