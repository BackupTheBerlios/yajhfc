Name:         yajhfc
Summary:      Yet Another Java HylaFAX Client
Version:      §VERSION§
Release:      1
License:      GNU General Public License (GPL)
Group:        Hardware/Fax
Source:       yajhfc-%{version}-rpmsrc.tgz
URL:          http://yajhfc.berlios.de/
Vendor:       Jonas Wolz
BuildRoot:    %{_tmppath}/%{name}-%{version}-build
Requires:     java >= 1.5.0
BuildArch:    noarch

%description
YajHFC is a client program for the HylaFAX fax server, implemented in Java.

 Features:
   * Faxing documents in PostScript, PDF and various other formats
   * Polling faxes
   * Support for generating cover pages from templates
   * Viewing sent and received faxes
   * Phone book (entries can optionally be read from SQL databases or LDAP directories)
   * Visible table columns may be selected in a dialog
   * Supports nine languages: English, French, German, Greek, Italian, Polish, Russian, Spanish and Turkish

%prep
%setup -n yajhfc-§VERSION§
%build
sh build.sh
%install
sh install.sh "%{buildroot}"

%clean
%__rm -rf "%{buildroot}"

%files
%defattr(-, root, root)
%doc doc/README.txt doc/faq.pdf doc/COPYING
/usr/bin/yajhfc
/usr/share/yajhfc
/usr/share/applications/yajhfc.desktop
/usr/share/pixmaps/yajhfc.xpm
/usr/share/pixmaps/yajhfc.png
/usr/share/man/man1/yajhfc.1.gz

%changelog
* Tue Jan 05 2010 - jonas.wolz@freenet.de
- initial RPM for YajHFC on openSUSE 
