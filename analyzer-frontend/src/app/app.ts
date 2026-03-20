import { Component, ChangeDetectorRef } from '@angular/core'; // 1. Dodano ChangeDetectorRef
import { FormsModule } from '@angular/forms';
import { Api, DirectoryAnalysisResult, FileMetrics } from './services/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  selectedFile: File | null = null;
  directoryPath: string = '';
  githubUrl: string = '';

  fileResult: FileMetrics | null = null;
  dirResult: DirectoryAnalysisResult | null = null;

  isLoading = false;
  errorMessage = '';

  // 2. Wstrzykujemy ChangeDetectorRef obok Twojego ApiService
  constructor(private apiService: Api, private cdr: ChangeDetectorRef) {}

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  analyzeSingleFile(event?: Event) {
    if (event) event.preventDefault();
    if (!this.selectedFile) return;
    this.prepareForRequest();

    this.apiService.analyzeFile(this.selectedFile).subscribe({
      next: (wynik) => {
        this.fileResult = wynik;
        this.isLoading = false;
        this.cdr.detectChanges(); // 3. WYMUSZAMY ODŚWIEŻENIE EKRANU!
      },
      error: (blad) => this.handleError(blad)
    });
  }

  analyzeDirectory(event?: Event) {
    if (event) event.preventDefault();
    if (!this.directoryPath) return;
    this.prepareForRequest();

    this.apiService.analyzeDirectory(this.directoryPath).subscribe({
      next: (wynik) => {
        this.dirResult = wynik;
        this.isLoading = false;
        this.cdr.detectChanges(); // 3. WYMUSZAMY ODŚWIEŻENIE EKRANU!
      },
      error: (blad) => this.handleError(blad)
    });
  }

  analyzeGithub(event?: Event) {
    if (event) event.preventDefault();

    console.log('Wysyłam zapytanie dla:', this.githubUrl);
    if (!this.githubUrl) return;
    this.prepareForRequest();

    this.apiService.analyzeRepository(this.githubUrl).subscribe({
      next: (wynik) => {
        console.log('Dane odebrane z Javy:', wynik);
        this.dirResult = wynik;
        this.isLoading = false;
        this.cdr.detectChanges(); // 3. WYMUSZAMY ODŚWIEŻENIE EKRANU!
      },
      error: (blad) => this.handleError(blad)
    });
  }

  private prepareForRequest() {
    this.isLoading = true;
    this.errorMessage = '';
    this.fileResult = null;
    this.dirResult = null;
  }

  private handleError(err: any) {
    this.isLoading = false;
    this.errorMessage = 'Wystąpił błąd komunikacji z serwerem.';
    console.error(err);
    this.cdr.detectChanges(); // Nawet przy błędzie każemy mu odświeżyć ekran
  }
}
