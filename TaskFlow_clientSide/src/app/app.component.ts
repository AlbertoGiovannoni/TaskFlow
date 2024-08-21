import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Importa FormsModule
import { HelloService } from './services/hello.service'; // Assicurati che il percorso sia corretto

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, FormsModule], // Aggiungi FormsModule qui
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  messageInput: string = ''; // Messaggio di input dell'utente
  responseMessage: string = ''; // Messaggio di risposta dal backend

  constructor(private helloService: HelloService) { }

  sendMessage(): void {
    this.helloService.sayHello(this.messageInput).subscribe(
      (response) => {
        this.responseMessage = response.message;
        console.log('Messaggio inviato con successo:', response.message);
      },
      (error) => {
        console.error('Errore:', error);
        console.error('Errore di parsing:', error.error.message || error.message);
      }
    );
  }
}