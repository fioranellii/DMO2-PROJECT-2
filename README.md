<div align="center">

# 📝 SafeMemo

**Aplicativo Android de anotações inteligentes**

Projeto acadêmico — Desenvolvimento Mobile II (DMO2)  
IFSP Campus Araraquara · Análise e Desenvolvimento de Sistemas  
Professor: Henrique Galati


</div>



## 👥 Integrantes

| Nome | RA |
|---|---|
| Hugo Mascherini Fioranelli | AQ3029565 |
| Ygor Soares da Silva | AQ3030563 |

---

## 💡 Sobre o projeto

O **SafeMemo** é um app de anotações que vai além do texto. Em vez de guardar só palavras, o usuário pode registrar memórias completas — com **foto tirada na hora**, **áudio gravado pelo microfone** e **texto** — tudo sincronizado em tempo real na nuvem via Firebase Firestore.

O diferencial está no uso dos **sensores nativos do dispositivo**, que tornam a experiência mais fluida e natural, sem botões extras.

---

## ✨ Funcionalidades

- 🔐 **Autenticação** — cadastro e login seguro com Firebase Auth (e-mail e senha)
- 🗒️ **Criar notas ricas** — título, descrição, foto da câmera e áudio do microfone em uma só nota
- 📋 **Lista de notas** — todas as notas do usuário carregadas do Firestore em tempo real
- 🔍 **Detalhe da nota** — visualiza a foto e reproduz o áudio gravado
- 👤 **Perfil** — tela com dados da conta e opção de logout
- 🔔 **Notificações locais** — o app notifica ao salvar uma nota com sucesso

### 📳 Sensores integrados

| Sensor | O que faz |
|---|---|
| **Acelerômetro** | Agitar o celular no formulário limpa os campos automaticamente |
| **Sensor de Luz** | Quando o ambiente está escuro, o fundo do formulário muda para modo escuro em tempo real |

---

## 🛠️ Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| Autenticação | Firebase Authentication |
| Banco de dados | Firebase Firestore |
| Câmera | Camera API — `ActivityResultContracts` |
| Áudio | `MediaRecorder` · `MediaPlayer` |
| Sensores | `SensorManager` — `TYPE_ACCELEROMETER` · `TYPE_LIGHT` |
| UI | ViewBinding · RecyclerView · Material Design |

---

---

## 🎬 Video Demonstrativo





https://github.com/user-attachments/assets/0e297f8a-2c41-4c35-a214-7a8e22db192b







---

## ⚙️ Como rodar

### Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17+
- Conta no [Firebase Console](https://console.firebase.google.com/)

### Configuração do Firebase

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com/)
2. Adicione um app Android com o package name `com.hugoygor.myapplication`
3. Baixe o `google-services.json` gerado e coloque dentro da pasta `app/`
4. Ative o **Firebase Authentication** com o provedor **E-mail/Senha**
5. Ative o **Cloud Firestore** e configure as regras de acordo com sua necessidade

### Executando

```bash
git clone https://github.com/seu-usuario/SafeMemo.git
cd SafeMemo
```

Abra o projeto no Android Studio, aguarde a sincronização do Gradle, conecte um dispositivo ou inicie um emulador e clique em **Run**.

### Permissões necessárias

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---



